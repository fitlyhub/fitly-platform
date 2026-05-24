-- 1. std_reference
create table if not exists std_reference
(
    std_reference_id uuid                    not null
        primary key,
    code             varchar(100)            not null
        unique,
    name             varchar(255)            not null,
    description      text,
    validation_type  varchar(100)            not null, -- e.g., 'DATA_TYPE', 'LIST', 'TABLE'
    is_active        boolean   default true  not null,
    created_at       timestamp default now() not null,
    updated_at       timestamp default now() not null,
    created_by       uuid,
    updated_by       uuid
);

-- 2. std_ref_item
create table if not exists std_ref_item
(
    std_ref_item_id  uuid                    not null
        primary key,
    std_reference_id uuid                    not null
        references std_reference (std_reference_id)
        on delete cascade,
    code             varchar(100)            not null,
    name             varchar(255)            not null,
    description      text,
    seq_no           integer   default 0     not null,
    is_active        boolean   default true  not null,
    created_at       timestamp default now() not null,
    updated_at       timestamp default now() not null,
    created_by       uuid,
    updated_by       uuid,
    unique (std_reference_id, code)
);

-- 3. std_validation_rule
create table if not exists std_validation_rule
(
    std_validation_rule_id uuid                    not null
        primary key,
    code                   varchar(100)            not null
        unique,
    name                   varchar(255)            not null,
    description            text,
    rule_type              varchar(100)            not null, -- e.g., 'SQL', 'SCRIPT', 'REGEX'
    rule_code              text                    not null,
    is_active              boolean   default true  not null,
    created_at             timestamp default now() not null,
    updated_at             timestamp default now() not null,
    created_by             uuid,
    updated_by             uuid
);

-- 4. std_hook
create table if not exists std_hook
(
    std_hook_id uuid                    not null
        primary key,
    code        varchar(100)            not null
        unique,
    name        varchar(255)            not null,
    description text,
    hook_type   varchar(100)            not null, -- e.g., 'JAVA', 'JAVASCRIPT', 'HTTP'
    hook_code   text                    not null,
    is_active   boolean   default true  not null,
    created_at  timestamp default now() not null,
    updated_at  timestamp default now() not null,
    created_by  uuid,
    updated_by  uuid
);

-- 5. std_table
create table if not exists std_table
(
    std_table_id uuid                    not null
        primary key,
    code         varchar(100)            not null
        unique, -- Physical DB Table Name
    name         varchar(255)            not null, -- Display Label
    description  text,
    is_view      boolean   default false not null,
    is_read_only boolean   default false not null,
    is_active    boolean   default true  not null,
    created_at   timestamp default now() not null,
    updated_at   timestamp default now() not null,
    created_by   uuid,
    updated_by   uuid
);

-- 6. std_column
create table if not exists std_column
(
    std_column_id          uuid                    not null
        primary key,
    std_table_id           uuid                    not null
        references std_table (std_table_id)
        on delete cascade,
    code                   varchar(100)            not null, -- Physical DB Column Name
    name                   varchar(255)            not null, -- Display Label
    description            text,
    std_reference_id       uuid                    not null
        references std_reference (std_reference_id),
    std_validation_rule_id uuid
        references std_validation_rule (std_validation_rule_id),
    is_mandatory           boolean   default false not null,
    is_identifier          boolean   default false not null,
    is_updateable          boolean   default true  not null,
    is_encrypted           boolean   default false not null,
    default_value          varchar(255),
    is_active              boolean   default true  not null,
    created_at             timestamp default now() not null,
    updated_at             timestamp default now() not null,
    created_by             uuid,
    updated_by             uuid,
    unique (std_table_id, code)
);

-- 7. std_page
create table if not exists std_page
(
    std_page_id  uuid                    not null
        primary key,
    code         varchar(100)            not null
        unique,
    name         varchar(255)            not null,
    description  text,
    page_type    varchar(100), -- e.g., 'TRANSACTION', 'REPORT', 'DASHBOARD'
    is_active    boolean   default true  not null,
    created_at   timestamp default now() not null,
    updated_at   timestamp default now() not null,
    created_by   uuid,
    updated_by   uuid
);

-- 8. std_section
create table if not exists std_section
(
    std_section_id    uuid                    not null
        primary key,
    std_page_id       uuid                    not null
        references std_page (std_page_id)
        on delete cascade,
    parent_section_id uuid
        references std_section (std_section_id)
        on delete set null,
    std_table_id      uuid
        references std_table (std_table_id),
    code              varchar(100)            not null,
    name              varchar(255)            not null,
    description       text,
    seq_no            integer   default 0     not null,
    is_single_row     boolean   default false not null,
    is_active         boolean   default true  not null,
    created_at        timestamp default now() not null,
    updated_at        timestamp default now() not null,
    created_by        uuid,
    updated_by        uuid,
    unique (std_page_id, code)
);

-- 9. std_field
create table if not exists std_field
(
    std_field_id   uuid                    not null
        primary key,
    std_section_id uuid                    not null
        references std_section (std_section_id)
        on delete cascade,
    std_column_id  uuid                    not null
        references std_column (std_column_id)
        on delete cascade,
    std_hook_id    uuid
        references std_hook (std_hook_id)
        on delete set null,
    code           varchar(100)            not null,
    name           varchar(255)            not null,
    description    text,
    seq_no         integer   default 0     not null,
    is_displayed   boolean   default true  not null,
    is_read_only   boolean   default false not null,
    is_mandatory   boolean   default false not null,
    display_length integer,
    is_active      boolean   default true  not null,
    created_at     timestamp default now() not null,
    updated_at     timestamp default now() not null,
    created_by     uuid,
    updated_by     uuid,
    unique (std_section_id, code)
);

-- 10. std_action
create table if not exists std_action
(
    std_action_id  uuid                    not null
        primary key,
    code           varchar(100)            not null
        unique,
    name           varchar(255)            not null,
    description    text,
    action_type    varchar(100)            not null, -- e.g., 'JAVA_CLASS', 'SQL_PROCEDURE', 'SCRIPT'
    procedure_name varchar(255),
    is_active      boolean   default true  not null,
    created_at     timestamp default now() not null,
    updated_at     timestamp default now() not null,
    created_by     uuid,
    updated_by     uuid
);

-- 11. std_action_param
create table if not exists std_action_param
(
    std_action_param_id uuid                    not null
        primary key,
    std_action_id       uuid                    not null
        references std_action (std_action_id)
        on delete cascade,
    code                varchar(100)            not null,
    name                varchar(255)            not null,
    description         text,
    seq_no              integer   default 0     not null,
    std_reference_id    uuid                    not null
        references std_reference (std_reference_id),
    is_mandatory        boolean   default false not null,
    default_value       varchar(255),
    is_active           boolean   default true  not null,
    created_at          timestamp default now() not null,
    updated_at          timestamp default now() not null,
    created_by          uuid,
    updated_by          uuid,
    unique (std_action_id, code)
);

-- 12. std_menu
create table if not exists std_menu
(
    std_menu_id    uuid                    not null
        primary key,
    parent_menu_id uuid
        references std_menu (std_menu_id)
        on delete set null,
    code           varchar(100)            not null
        unique,
    name           varchar(255)            not null,
    description    text,
    seq_no         integer   default 0     not null,
    action_type    varchar(100), -- e.g., 'PAGE', 'ACTION', 'FOLDER'
    std_page_id    uuid
        references std_page (std_page_id)
        on delete set null,
    std_action_id  uuid
        references std_action (std_action_id)
        on delete set null,
    is_active      boolean   default true  not null,
    created_at     timestamp default now() not null,
    updated_at     timestamp default now() not null,
    created_by     uuid,
    updated_by     uuid
);

-- 13. std_translate
create table if not exists std_translate
(
    std_translate_id uuid                    not null
        primary key,
    table_name       varchar(100)            not null,
    record_id        uuid                    not null,
    column_name      varchar(100)            not null,
    lang             varchar(50)             not null,
    translated_text  text                    not null,
    is_active        boolean   default true  not null,
    created_at       timestamp default now() not null,
    updated_at       timestamp default now() not null,
    created_by       uuid,
    updated_by       uuid,
    unique (table_name, record_id, column_name, lang)
);

-- 14. std_access
create table if not exists std_access
(
    std_access_id uuid                    not null
        primary key,
    sys_role_id   uuid                    not null,
    access_type   varchar(100)            not null, -- e.g., 'PAGE', 'ACTION', 'TABLE', 'COLUMN'
    record_id     uuid                    not null,
    is_read_only  boolean   default false not null,
    is_permitted  boolean   default true  not null,
    is_active     boolean   default true  not null,
    created_at    timestamp default now() not null,
    updated_at    timestamp default now() not null,
    created_by    uuid,
    updated_by    uuid,
    unique (sys_role_id, access_type, record_id)
);
