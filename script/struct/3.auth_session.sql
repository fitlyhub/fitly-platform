create table if not exists sys_tenant_profile
(
    sys_tenant_profile_id uuid                    not null
        primary key,
    code                  varchar(100),
    name                  varchar(255),
    domain                varchar(255),
    logo_url              text,
    timezone              varchar(100)            not null default 'Asia/Ho_Chi_Minh',
    default_language      varchar(50)             not null default 'vi_VN',
    support_languages     jsonb                   not null default '["vi_VN", "en_US"]'::jsonb,
    is_active             boolean   default true  not null,
    created_at            timestamp default now() not null,
    updated_at            timestamp default now() not null
);

alter table sys_tenant_profile
    add column if not exists code varchar(100);

alter table sys_tenant_profile
    add column if not exists name varchar(255);

alter table sys_tenant_profile
    add column if not exists domain varchar(255);

alter table sys_tenant_profile
    add column if not exists is_active boolean not null default true;

create unique index if not exists ux_sys_tenant_profile_code
    on sys_tenant_profile (code)
    where code is not null;

create table if not exists auth_session
(
    auth_session_id   uuid                           not null
        primary key,
    tenant_id         uuid                           not null,
    sys_user_id       uuid                           not null,
    sys_position_id   uuid,
    session_token_hash text                          not null,
    is_active         boolean   default true         not null,
    issued_at         timestamp default now()        not null,
    expires_at        timestamp without time zone    not null,
    last_access_at    timestamp without time zone,
    revoked_at        timestamp without time zone,
    ip_address        varchar(64),
    user_agent        text,
    created_at        timestamp default now()        not null,
    updated_at        timestamp default now()        not null
);

create unique index if not exists ux_auth_session_token_hash
    on auth_session (session_token_hash);

create index if not exists ix_auth_session_user_active
    on auth_session (tenant_id, sys_user_id, is_active);

create index if not exists ix_auth_session_expires_at
    on auth_session (expires_at);
