# Fitly Platform Agent Coding Guide

You are an expert software developer working on **Fitly Platform**, a modular monolith ERP platform built with
**Java 25**,
**Spring Boot**,
**Maven multi-module architecture**,
**PostgreSQL**,
and Fitly's own request/processor/DAO conventions.

Your job is to generate code that is simple, explicit, maintainable, and suitable for a dynamic ERP system. Do **not** over-engineer with excessive Spring layers. Fitly prefers a practical architecture that keeps the ease of ERP process coding, but avoids mixing SQL, business logic, request flow, and response formatting into an unmaintainable class.

---

## 0. Product Inspiration and Legal Boundaries

Fitly is conceptually inspired by mature ERP patterns found in systems such as iDempiere and Odoo, but it must be implemented as an original platform with its own codebase, schema, terminology, UI, and architecture.

The agent may use third-party ERP products only as high-level conceptual references for ideas such as:

- metadata-driven UI
- role/access control
- workflow/action execution
- modular ERP navigation
- master/detail documents
- configurable fields, references, validation rules, hooks, and actions

The agent must **not** copy source code, database schema, UI layout, documentation text, icons, logos, colors, screenshots, naming conventions, or proprietary implementation details from iDempiere, Odoo, SAP, or any other third-party product.

Use inspiration only for concepts. Fitly implementation must be original.

---

## 1. Current Technology Stack

Use the following assumptions unless the user explicitly says otherwise:

- Java 25
- Spring Boot
- Maven multi-module project
- PostgreSQL
- Native JDBC / PreparedStatement preferred in DAO layer
- REST API only
- Server-side business logic
- Multi-tenant ERP architecture
- Fitly core controls request execution, transaction boundaries, context, connection, language, and trace information
- Avoid Hibernate/JPA unless the existing target module already uses it
- Avoid reactive programming unless explicitly requested
- Avoid async by default
- Prefer virtual threads when relevant, but do not introduce async complexity
---

## 2. Existing Fitly Coding Style Must Be Preserved

The project currently uses package names such as:

```text
vn.fitly.foundation
vn.fitly.common
vn.fitly.iam
vn.fitly.infrastructure
```

The project currently has Fitly-specific base classes and wrappers such as:

```text
RequestExecutor
BaseResponse<T>
AFitlyProcessor<RQ, RP>
DaoFactory
FitlyBussinessException
FitlyRuntimeException
ErrorStatus
DefaultSystemMessage
```

The agent must reuse the existing Fitly classes instead of inventing replacements such as:

```text
ApiResponse
RuleException
NotFoundException
Repository
UseCaseHandler
RequestContextHelper
```

unless the user explicitly asks to create or rename them.

---

## 3. Standard File Header

New Java files should follow the existing Fitly header style:

```java
/**
 * Project: Fitly Platform
 * Author:  <curent author>
 * Date:    <current date>
 * Time:    <current time>
 * * Copyright (c) 2026 Fitly. All rights reserved.
 * Licensed under the Apache License 2.0.
 */
```

Do not remove or rewrite existing headers unless asked.

---

## 4. Core Architectural Philosophy

Fitly should avoid both extremes:

```text
Bad extreme 1: SvrProcess-style chaos
- SQL, validation, workflow, permission, response, and side effects mixed in one class
- Easy at first
- Hard to maintain later

Bad extreme 2: Over-layered Spring ceremony
- Controller -> Processor -> Service -> Repository -> Mapper -> Entity -> DTO for every tiny use case
- Too many files
- Too much boilerplate
- Slow development
```

Fitly uses a **Processor-first architecture**:

```text
Default flow:
Controller
    -> RequestExecutor
        -> Processor
            -> DAO

When logic becomes complex or reusable:
Controller
    -> RequestExecutor
        -> Processor
            -> Service
                -> DAO
```

`Service` is optional extraction, not a mandatory layer for every API.

---

## 5. Responsibility Rules

### 5.1 Controller

Controller must be thin.

Allowed responsibilities:

- Receive HTTP request
- Read path/query/body parameters
- Create the correct processor when the current project style does that
- Call `RequestExecutor`
- Return `BaseResponse<T>`

Not allowed:

- SQL
- Business logic
- Transaction handling
- Complex validation
- Calling DAO directly

Preferred pattern based on current codebase:

```java
@RestController
@RequestMapping("/auth/v1")
public class AuthController {

    @Autowired
    RequestExecutor executor;

    @PostMapping("/login")
    public BaseResponse<LoginResponse> login(@RequestBody LoginRequest request) {
        return executor.process(new Login(request));
    }
}
```

Important:

- Do not inject every processor as a Spring bean unless the existing module already follows that style.
- It is acceptable in the current style for the controller to create a processor instance with `new Login(request)` and pass it to `RequestExecutor`.

---

### 5.2 Processor

Processor represents one API use case or one executable ERP action.

In current Fitly style, a processor usually extends:

```java
AFitlyProcessor<RequestType, ResponseType>
```

Allowed responsibilities:

- Validate request in `validate()`
- Execute use-case flow in `processInternal()`
- Call DAO directly for simple logic
- Call service for complex/reusable logic
- Create response object
- Throw Fitly business/runtime exceptions

Not allowed:

- SQL directly in processor
- Opening/closing DB connections
- Commit/rollback transaction
- Calling another processor
- Becoming a huge unrelated class

Hard rule:

```text
Processor may call DAO.
Processor may call Service.
Processor must not call another Processor.
Processor must not contain SQL.
```

Example aligned with current style:

```java
public class Login extends AFitlyProcessor<LoginRequest, LoginResponse> {

    public Login(LoginRequest request) {
        super(request);
    }

    @Override
    protected void validate() throws Exception {

        if (request == null) {
            throw new FitlyBussinessException(ErrorStatus.REQUEST_INVALID, DefaultSystemMessage.REQUEST_INVALID.name());
        }

        if (StringUtils.isBlank(request.getUsername())) {
            throw new FitlyBussinessException(ErrorStatus.REQUEST_INVALID, DefaultSystemMessage.REQUEST_INVALID.name());
        }

        if (StringUtils.isBlank(request.getPassword())) {
            throw new FitlyBussinessException(ErrorStatus.REQUEST_INVALID, DefaultSystemMessage.REQUEST_INVALID.name());
        }
    }

    @Override
    protected LoginResponse processInternal() throws Exception {

        UserDao userDao = DaoFactory.getDao(UserDao.class);
        User user = userDao.getUserByUsername(request.getUsername());

        if (user == null) {
            throw new FitlyBussinessException(ErrorStatus.UNAUTHORIZED, "LOGIN_FAILED");
        }

        if (!user.isActive()) {
            throw new FitlyBussinessException(ErrorStatus.RULE_EXCEPTION, "USER_DEACTIVE");
        }

        if (!BCrypt.checkpw(request.getPassword(), user.getPassword())) {
            throw new FitlyBussinessException(ErrorStatus.UNAUTHORIZED, "LOGIN_FAILED");
        }

        LoginResponse response = new LoginResponse();
        response.setAccessToken(generateJwtToken(user));
        response.setRefreshToken(generateRefreshToken(user));
        response.setUserId(user.getUserId().toString());

        RoleDao roleDao = DaoFactory.getDao(RoleDao.class);
        response.setPositionList(roleDao.getUserPositions(user.getUserId()));

        return response;
    }
}
```

Notes:

- For login, avoid revealing whether username or password was wrong. Prefer a generic `LOGIN_FAILED` / `USER_UNAUTHORIZED` error unless the user explicitly wants detailed login errors.
- Do not log raw passwords, password hashes, JWT tokens, or refresh tokens.
- Mock refresh tokens are acceptable only in temporary code and must be clearly marked as temporary.

---

### 5.3 Service

Service is not mandatory. Create service only when useful.

Create a service when one or more are true:

- Logic is reused by multiple processors
- Processor becomes too long or hard to read
- Processor calls many DAOs
- Logic belongs to a business concept, not just one API
- Logic includes workflow, action, hook, document status, accounting, inventory, permission, notification, or cross-module coordination

Allowed responsibilities:

- Business logic
- Business validation
- Coordination between DAOs
- Calling other services
- Workflow/action/hook execution
- Permission logic
- Document status transitions

Not allowed:

- SQL directly in service
- Opening/closing DB connections
- Commit/rollback transaction
- Returning HTTP-specific controller objects

Hard rule:

```text
Service may call DAO.
Service may call another Service.
DAO must not call Service.
```

Do not create a service just because Spring projects usually have services.

---

### 5.4 DAO

DAO is the only place where SQL should live.

Allowed responsibilities:

- SQL statements
- PreparedStatement binding
- ResultSet mapping
- Insert/update/delete/select
- Database-specific query optimization

Not allowed:

- Business decisions
- Permission decisions
- Workflow decisions
- Calling service
- Commit/rollback transaction
- Returning `BaseResponse<T>`

DAO should use the connection from Fitly request context/core, not create its own transaction boundary.

Example pattern:

```java
public class UserDao {

    public User getUserByUsername(String username) {

        if (StringUtils.isBlank(username)) {
            throw new FitlyRuntimeException(ErrorStatus.INTERNAL_ERROR, "username is required when querying user by username.");
        }

        String sql = """
                select sys_user_id,
                       username,
                       password,
                       is_active
                  from sys_user
                 where username = ?
                """;

        try (PreparedStatement ps = RequestContext.getConnection().prepareStatement(sql)) {
            ps.setString(1, username);

            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) {
                    return null;
                }

                User user = new User();
                user.setUserId((UUID) rs.getObject("sys_user_id"));
                user.setUsername(rs.getString("username"));
                user.setPassword(rs.getString("password"));
                user.setActive(rs.getBoolean("is_active"));
                return user;
            }
        } catch (SQLException e) {
            throw new FitlyRuntimeException(ErrorStatus.INTERNAL_ERROR, "Failed to query user by username.", e);
        }
    }
}
```

Security note:

- Do not include password value, password hash, token, or sensitive PII in exception messages or logs.

---

## 6. Transaction and Connection Rules

Fitly core owns transaction boundaries.

Expected model:

```text
One HTTP request/action execution
    = one request context
    = one DB connection
    = one transaction boundary
```

Do not generate code that manually commits or rolls back inside controller, processor, service, or DAO.

Do not generate this inside normal business code:

```java
connection.commit();
connection.rollback();
connection.close();
```

Only Fitly core infrastructure such as `RequestExecutor`, datasource, or request context may handle transaction lifecycle.

Do not add Spring `@Transactional` by default. Use it only if the user explicitly asks or the target module already uses Spring-managed transaction boundaries.

---

## 7. Layer Decision Guide

Use this decision table before generating new classes:

| Situation | Recommended Flow |
|---|---|
| Simple read API | Controller -> RequestExecutor -> Processor -> DAO |
| Simple login/check/create/update | Controller -> RequestExecutor -> Processor -> DAO |
| Use case calls many DAOs | Controller -> RequestExecutor -> Processor -> Service -> DAO |
| Logic reused by multiple processors | Extract Service |
| Workflow/action/hook/document status logic | Use Service |
| Permission/access logic reused widely | Use Service |
| SQL required | Put SQL in DAO |
| Generic helper without business state | Use helper carefully only if existing project style allows |

Default to fewer layers first.

---

## 8. Naming Rules

### 8.1 Processor Names

Current project may use short action-like processor names under version packages:

```text
vn.fitly.iam.processor.v1.Login
vn.fitly.iam.processor.v1.RefreshToken
vn.fitly.iam.processor.v1.CreateUser
```

This is allowed.

When creating new processors, follow existing module naming. Do not force `LoginProcessor` if the module currently uses `Login`.

Acceptable names:

```text
Login
CreateUser
UpdateUser
GetUser
CreateSaleOrder
CompleteSaleOrder
LoadWindowMetadata
ExecuteAction
```

Avoid vague names:

```text
UserProcessor
OrderProcessor
CommonProcessor
BaseProcessorForEverything
```

---

### 8.2 DAO Names

Use clear DAO names:

```text
UserDao
RoleDao
PermissionDao
SaleOrderDao
SaleOrderLineDao
WindowMetadataDao
ReferenceDao
```

Do not create `Repository` classes unless the existing module already uses that naming.

---

### 8.3 Model, Request, Response Names

Current project uses:

```text
model
request
response
```

Examples:

```text
vn.fitly.iam.model.User
vn.fitly.iam.request.LoginRequest
vn.fitly.iam.response.LoginResponse
```

Follow this style. Do not rename to DTO/entity packages unless the user asks.

---

## 9. Code Style Rules

### 9.1 Braces Enforcement

Always use curly braces for all control flow statements.

Correct:

```java
if (user == null) {
    throw new FitlyBussinessException(ErrorStatus.NOTFOUND, "USER_NOT_FOUND");
}
```

Wrong:

```java
if (user == null) throw new FitlyBussinessException(ErrorStatus.NOTFOUND, "USER_NOT_FOUND");
```

This applies to:

- `if`
- `for`
- `while`
- `do while`
- `try/catch/finally`

---

### 9.2 Early Return / Guard Clauses

Use early return or early throw for invalid cases.

Prefer:

```java
if (request == null) {
    throw new FitlyBussinessException(ErrorStatus.REQUEST_INVALID, DefaultSystemMessage.REQUEST_INVALID.name());
}

if (StringUtils.isBlank(request.getUsername())) {
    throw new FitlyBussinessException(ErrorStatus.REQUEST_INVALID, DefaultSystemMessage.REQUEST_INVALID.name());
}
```

Avoid deep nesting:

```java
if (request != null) {
    if (!StringUtils.isBlank(request.getUsername())) {
        // main logic
    }
}
```

Avoid `else` and `else if` unless it is clearly simpler and more readable.

Do not use switch expressions or pattern matching unless the user explicitly requests it.

---

### 9.3 POJO Instead of Record

Do not use Java `record`.

All request, response, model, and payload classes must be standard Java classes with explicit getters and setters.

Correct:

```java
public class LoginRequest {

    private String username;

    private String password;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
```

Wrong:

```java
public record LoginRequest(String username, String password) {
}
```

---

### 9.4 BigDecimal for Money

Always use `BigDecimal` for:

- price
- amount
- currency
- tax
- discount
- payment
- balance
- exchange rate if precision is important

Never use `float` or `double` for financial values.

---

### 9.5 Null Handling

Returning `null` is allowed for simplicity.

Because null is allowed, always check null explicitly before property/method access.

Correct:

```java
User user = userDao.getUserByUsername(username);
if (user == null) {
    throw new FitlyBussinessException(ErrorStatus.NOTFOUND, "USER_NOT_FOUND");
}

String userId = user.getUserId().toString();
```

Do not use `Optional` unless existing code in the target area already uses it.

---

## 10. SQL Rules

### 10.1 SQL Must Stay in DAO

Do not put SQL in:

- Controller
- Processor
- Service
- Request/response/model classes

SQL belongs in DAO only.

---

### 10.2 Use Java Text Blocks for Multiline SQL

Correct:

```java
String sql = """
        select sys_user_id,
               username,
               is_active
          from sys_user
         where username = ?
        """;
```

---

### 10.3 No Dynamic SQL Concatenation with User Input

Never concatenate user input or request values directly into SQL.

Wrong:

```java
String sql = "select * from sys_user where username = '" + request.getUsername() + "'";
```

Correct:

```java
String sql = """
        select *
          from sys_user
         where username = ?
        """;

PreparedStatement ps = connection.prepareStatement(sql);
ps.setString(1, request.getUsername());
```
Prefer `StringBuilder` for dynamic SQL. Do not use `+` or `+=` for building SQL except for tiny static-only fragments. Dynamic parts such as column names, sort fields, direction, table aliases, and filters must be resolved through explicit whitelist methods before being appended.
```

The resolver must reject unknown values.

---

## 11. Exception and Logging Rules

### 11.1 Use Existing Fitly Exceptions

Prefer existing exceptions:

```text
FitlyBussinessException
FitlyRuntimeException
```

Use existing status/message conventions:

```text
ErrorStatus.REQUEST_INVALID
ErrorStatus.NOTFOUND
ErrorStatus.UNAUTHORIZED
ErrorStatus.RULE_EXCEPTION
ErrorStatus.INTERNAL_ERROR
DefaultSystemMessage.REQUEST_INVALID.name()
DefaultSystemMessage.INTERNAL_ERROR.name()
```

Do not invent new exception classes unless asked.

---

### 11.2 Detailed but Safe Exceptions

Exceptions must include useful context, but must not leak secrets.

Good:

```java
throw new FitlyRuntimeException(ErrorStatus.INTERNAL_ERROR, "Failed to query user positions. userId=" + userId, e);
```

Bad:

```java
throw new RuntimeException("Error");
```

Bad because it leaks sensitive data:

```java
throw new FitlyRuntimeException(ErrorStatus.INTERNAL_ERROR, "Login failed. password=" + request.getPassword(), e);
```

---

### 11.3 Do Not Leak Sensitive Data

Never log or expose:

- password
- password hash
- JWT token
- refresh token
- credit card number
- secret key
- private key
- authorization header
- sensitive PII

For login errors, prefer generic messages such as:

```text
LOGIN_FAILED
USER_UNAUTHORIZED
```

rather than revealing exactly whether the username or password was wrong.

---

## 12. Spring Boot Rules

### 12.1 Do Not Force Spring Injection Everywhere

Fitly uses Spring Boot, but not every class must be a Spring bean.

Current pattern allows:

```java
return executor.process(new Login(request));
```

Do not automatically convert processors to `@Component` constructor-injected beans unless the user asks.

---

### 12.2 Controller Injection

If editing existing controllers, preserve existing style unless asked to refactor.

Current code may use:

```java
@Autowired
RequestExecutor executor;
```

This is acceptable for current project style.

If creating a completely new controller and no existing style is obvious, constructor injection is preferred:

```java
@RestController
@RequestMapping("/example/v1")
public class ExampleController {

    private final RequestExecutor executor;

    public ExampleController(RequestExecutor executor) {
        this.executor = executor;
    }
}
```

But do not refactor existing working controllers only to change injection style.

---

### 12.3 Avoid Heavy Spring Magic

Do not introduce unnecessary:

- complex AOP
- custom annotations
- hidden lifecycle behavior
- Spring Data repositories
- reflection-heavy frameworks
- implicit transaction behavior

Keep code explicit and easy to trace.

---

## 13. Module and Package Naming

### 13.1 Maven Module Naming

Use `kebab-case`.

Fitly modules should start with `fitly-` and use singular nouns when adding new modules.

Good:

```text
fitly-common
fitly-foundation
fitly-infrastructure
fitly-iam
fitly-studio
fitly-launcher
```

Bad:

```text
fitly-entities
fitly-models
fitly-utils
fitly-configurations
```

Do not create generic modules without permission.

---

### 13.2 Package Naming

Packages must be lowercase.

Follow existing singular package style:

```text
vn.fitly.iam.controller
vn.fitly.iam.processor.v1
vn.fitly.iam.dao
vn.fitly.iam.model
vn.fitly.iam.request
vn.fitly.iam.response
vn.fitly.foundation.context
vn.fitly.foundation.processor
vn.fitly.foundation.response
vn.fitly.common.exception
```

Do not rename existing packages from `request/response/model` to `dto/entity` unless asked.

---

## 14. Fitly Dynamic ERP Rules

Fitly is a dynamic ERP platform. Many screens, fields, actions, references, validations, and workflows are metadata-driven.

Current metadata naming direction:

```text
std_table
std_column
std_page
std_section
std_field
std_menu
std_reference
std_ref_item
std_validation_rule
std_hook
std_action
std_action_param
std_translate
std_access
```

Use `action`, not `process`, for user-visible executable operations.

Use `hook` for callout-like server-side field/data logic.

Suggested metadata loading flow:

```text
Controller
    -> RequestExecutor
        -> LoadPageMetadata
            -> PageMetadataService
                -> PageMetadataDao
                -> PermissionService
                -> ReferenceService
                -> TranslationService
```

Suggested action execution flow:

```text
Controller
    -> RequestExecutor
        -> ExecuteAction
            -> ActionService
                -> PermissionService
                -> WorkflowService
                -> BusinessActionHandler
                -> DAO
```

Do not put dynamic metadata SQL directly in processors.

---

## 15. Agent Workflow Rules

Before changing code, the agent must understand the current project state.

### 15.1 Always Inspect Before Editing

Before generating or editing code:

1. Inspect the relevant module structure.
2. Inspect existing classes with similar responsibility.
3. Follow existing package names and conventions.
4. Reuse existing base classes, exceptions, response wrappers, request context, and DAO factory.
5. Do not invent new frameworks or patterns if the project already has equivalents.

Do not create duplicate classes such as:

```text
ApiResponse2
BaseResponseNew
UserDaoImplNew
RequestContextHelper
FitlyExceptionV2
```

unless explicitly requested.

---

### 15.2 Maintain an Agent Progress File

For larger tasks, create or update this file at the repository root:

```text
.agent/FITLY_AGENT_PROGRESS.md
```

The file should contain:

```markdown
# Fitly Agent Progress

## Current Task
- Task: <short task description>
- Started At: <date/time if available>
- Agent: <Gemini/Codex/Other>

## Decisions
- <important architectural or naming decisions>

## Files Inspected
- <file path> - <why it was inspected>

## Files Created
- <file path> - <purpose>

## Files Modified
- <file path> - <summary of changes>

## Pending Work
- <what remains to be done>

## Warnings / Assumptions
- <anything uncertain or assumed>
```

For every non-trivial task, update this file after meaningful changes.

This allows the next agent run to know what happened before.

---

### 15.3 Use Small, Traceable Changes

Do not rewrite large areas without permission.

Do not rename packages, modules, public classes, or response wrappers unless the user explicitly asked.

Do not convert the whole project to a new pattern.

Make the smallest coherent change that satisfies the request.

---

### 15.4 Stop and Report When Blocked

If required information is missing, do not guess wildly.

Report clearly:

```text
Blocked because:
- Could not find RequestContext class
- Existing DAO factory method name is unclear
- There are two UserDao variants

Recommended next step:
- Confirm which class should be used
```

If the user asked for best effort, proceed with explicit assumptions and record them in `.agent/FITLY_AGENT_PROGRESS.md`.

---

## 16. Code Generation Checklist

Before finalizing code, verify:

- [ ] Reused existing Fitly classes instead of inventing replacements
- [ ] Controller is thin
- [ ] Controller returns `BaseResponse<T>` if that is the module standard
- [ ] Processor extends `AFitlyProcessor<RQ, RP>` if that is the module standard
- [ ] Processor has validation in `validate()` when using `AFitlyProcessor`
- [ ] Main logic is in `processInternal()` when using `AFitlyProcessor`
- [ ] No SQL in controller/processor/service
- [ ] No processor calling another processor
- [ ] DAO does not call service
- [ ] No direct transaction commit/rollback outside Fitly core
- [ ] No unnecessary Spring magic
- [ ] No Java records
- [ ] Request/response/model classes have getters and setters
- [ ] All control flow statements use braces
- [ ] Guard clauses are used for validation
- [ ] No unnecessary `else` nesting
- [ ] No `float` or `double` for money
- [ ] No user input concatenated into SQL
- [ ] SQL uses PreparedStatement parameters
- [ ] Exceptions include useful but safe context
- [ ] No sensitive data logged or exposed
- [ ] New service was created only if justified
- [ ] Progress file updated for non-trivial tasks

---

## 17. Preferred Implementation Patterns

### 17.1 Simple API Pattern

```text
Controller -> RequestExecutor -> Processor -> DAO
```

Controller:

```java
@RestController
@RequestMapping("/auth/v1")
public class AuthController {

    @Autowired
    RequestExecutor executor;

    @PostMapping("/login")
    public BaseResponse<LoginResponse> login(@RequestBody LoginRequest request) {
        return executor.process(new Login(request));
    }
}
```

Processor:

```java
public class Login extends AFitlyProcessor<LoginRequest, LoginResponse> {

    public Login(LoginRequest request) {
        super(request);
    }

    @Override
    protected void validate() throws Exception {
        if (request == null) {
            throw new FitlyBussinessException(ErrorStatus.REQUEST_INVALID, DefaultSystemMessage.REQUEST_INVALID.name());
        }
    }

    @Override
    protected LoginResponse processInternal() throws Exception {
        UserDao userDao = DaoFactory.getDao(UserDao.class);
        User user = userDao.getUserByUsername(request.getUsername());

        if (user == null) {
            throw new FitlyBussinessException(ErrorStatus.UNAUTHORIZED, "LOGIN_FAILED");
        }

        LoginResponse response = new LoginResponse();
        response.setUserId(user.getUserId().toString());
        return response;
    }
}
```

---

### 17.2 Complex API Pattern

```text
Controller -> RequestExecutor -> Processor -> Service -> DAO
```

Processor:

```java
public class CompleteSaleOrder extends AFitlyProcessor<CompleteSaleOrderRequest, CompleteSaleOrderResponse> {

    public CompleteSaleOrder(CompleteSaleOrderRequest request) {
        super(request);
    }

    @Override
    protected void validate() throws Exception {
        if (request == null) {
            throw new FitlyBussinessException(ErrorStatus.REQUEST_INVALID, DefaultSystemMessage.REQUEST_INVALID.name());
        }

        if (request.getSaleOrderId() == null) {
            throw new FitlyBussinessException(ErrorStatus.REQUEST_INVALID, "SALE_ORDER_ID_REQUIRED");
        }
    }

    @Override
    protected CompleteSaleOrderResponse processInternal() throws Exception {
        SaleOrderService saleOrderService = new SaleOrderService();
        saleOrderService.complete(request.getSaleOrderId());

        CompleteSaleOrderResponse response = new CompleteSaleOrderResponse();
        response.setSaleOrderId(request.getSaleOrderId());
        response.setDocumentStatus("COMPLETED");
        return response;
    }
}
```

Note:

- If services need dependencies, follow the existing project style. Use `DaoFactory` for DAOs if that is the current convention.
- Do not create a Spring bean service unless the surrounding module already uses Spring-managed services or the user asks.

---

## 18. Final Rule Summary

The most important rules:

```text
1. Follow existing Fitly code style before introducing new patterns.
2. Use Spring Boot as runtime framework, but do not force Spring ceremony everywhere.
3. Default flow: Controller -> RequestExecutor -> Processor -> DAO.
4. Extract Service only when logic is complex or reusable.
5. Processor must not contain SQL.
6. Processor must not call another Processor.
7. DAO owns SQL and ResultSet mapping.
8. DAO must not contain business logic.
9. Transaction and connection lifecycle belong to Fitly core.
10. Use BaseResponse, AFitlyProcessor, DaoFactory, and Fitly exceptions if they already exist.
11. Keep code explicit, boring, and easy to debug.
12. Maintain .agent/FITLY_AGENT_PROGRESS.md for larger tasks.
```