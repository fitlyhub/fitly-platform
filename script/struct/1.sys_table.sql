create table if not exists sys_org
(
    sys_org_id      uuid                    not null
        primary key,
    parent_org_id   uuid references sys_org (sys_org_id),
    code            varchar(100)            not null
        unique,
    name            varchar(255)            not null,
    org_type        varchar(100)            not null,
    hierarchy_path  text,
    is_legal_entity boolean   default false not null,
    is_active       boolean   default true  not null,
    created_at      timestamp default now() not null,
    updated_at      timestamp default now() not null,
    created_by      uuid,
    updated_by      uuid
);

create table if not exists sys_segment
(
    sys_segment_id    uuid                    not null
        primary key,
    parent_segment_id uuid references sys_segment (sys_segment_id),
    code              varchar(100)            not null unique,
    name              varchar(255)            not null,
    hierarchy_path    text,
    is_active         boolean   default true  not null,
    created_at        timestamp default now() not null,
    updated_at        timestamp default now() not null,
    created_by        uuid                    not null,
    updated_by        uuid                    not null
);

create table if not exists sys_department
(
    sys_department_id    uuid                    not null
        primary key,
    sys_org_id           uuid                    not null references sys_org (sys_org_id),
    parent_department_id uuid references sys_department (sys_department_id),
    code                 varchar(100)            not null,
    name                 varchar(255)            not null,
    hierarchy_path       text,
    is_active            boolean   default true  not null,
    created_at           timestamp default now() not null,
    updated_at           timestamp default now() not null,
    created_by           uuid,
    updated_by           uuid,
    unique (sys_org_id, code)
);

create table if not exists sys_position
(
    sys_position_id uuid                    not null
        primary key,
    code            varchar(100)            not null unique,
    name            varchar(255)            not null,
    description     text,
    is_active       boolean   default true  not null,
    created_at      timestamp default now() not null,
    updated_at      timestamp default now() not null,
    created_by      uuid,
    updated_by      uuid
);

create table if not exists sys_role
(
    sys_role_id uuid                    not null
        primary key,
    code        varchar(100)            not null unique,
    name        varchar(255)            not null,
    description text,
    is_active   boolean   default true  not null,
    created_at  timestamp default now() not null,
    updated_at  timestamp default now() not null,
    created_by  uuid                    not null,
    updated_by  uuid                    not null
);

create table if not exists sys_position_role
(
    sys_position_role_id uuid                    not null
        primary key,
    sys_position_id      uuid                    not null references sys_position (sys_position_id),
    sys_role_id          uuid                    not null references sys_role (sys_role_id),
    is_active            boolean   default true  not null,
    created_at           timestamp default now() not null,
    updated_at           timestamp default now() not null,
    created_by           uuid                    not null,
    updated_by           uuid                    not null,
    unique (sys_position_id, sys_role_id)
);

create table if not exists sys_user
(
    sys_user_id       uuid                    not null
        primary key,
    sys_org_id        uuid                    not null references sys_org (sys_org_id), -- org for hr
    sys_department_id uuid references sys_department (sys_department_id),
    username          varchar(255)            not null
        unique,
    password          varchar(255)            not null,
    first_name        varchar(255),
    last_name         varchar(255),
    full_name         text,
    email             varchar(255),
    phone             varchar(50),
    avatar_url        text,
    is_active         boolean   default true  not null,
    is_locked         boolean   default false not null,
    last_login_at     timestamp,
    created_at        timestamp default now() not null,
    updated_at        timestamp default now() not null,
    created_by        uuid,
    updated_by        uuid
);


create table if not exists sys_user_position
(
    sys_user_position_id  uuid                    not null
        primary key,
    sys_user_id           uuid                    not null
        references sys_user (sys_user_id),
    sys_position_id       uuid                    not null
        references sys_position (sys_position_id),
    sys_org_id            uuid                    not null references sys_org (sys_org_id),
    org_scope_type        VARCHAR(50)             not null,
    sys_segment_id        uuid references sys_segment (sys_segment_id),
    segment_scope_type    VARCHAR(50)             not null,
    sys_department_id     uuid references sys_department (sys_department_id),
    department_scope_type VARCHAR(50)             not null,
    is_default            boolean   default false not null,
    is_active             boolean   default true  not null,
    created_at            timestamp default now() not null,
    updated_at            timestamp default now() not null,
    created_by            uuid                    not null,
    updated_by            uuid                    not null
);
