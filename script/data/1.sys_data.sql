INSERT INTO sys_tenant_profile (sys_tenant_profile_id, code, name, logo_url, timezone, default_language, support_languages)
VALUES (
    uuidv7(),
    'default',
    'Default Tenant',
    NULL,
    'Asia/Ho_Chi_Minh',
    'vi_VN',
    '["vi_VN", "en_US"]'::jsonb
)
on conflict do nothing;

update sys_tenant_profile
set code = coalesce(code, 'default'),
    name = coalesce(name, 'Default Tenant'),
    updated_at = now()
where code is null
   or name is null;

insert into sys_user(sys_user_id, username, password, first_name, last_name, full_name, email, phone, avatar_url,
                     is_active, is_locked, last_login_at, created_at, updated_at, created_by, updated_by)
values ('019e43f9-4135-79ac-8741-551fa0b99f6d', 'admin', '$2a$10$9s1eJzgZjkBoq6.9ifLlSevYFhBwRUd.ywZ8h6uAF35m/MNMAY5NO', 'Admin', 'Fitly', 'Fitly Admin Default', null, null, null,
        true, false, null, now(), now(), '019e43f9-4135-79ac-8741-551fa0b99f6d', '019e43f9-4135-79ac-8741-551fa0b99f6d')
on conflict (sys_user_id) do nothing;

insert into sys_orgtype(sys_orgtype_id, code, name, is_active, created_at, updated_at, created_by, updated_by)
values (uuidv7(), 'DEFAULT', 'Default', true, now(), now(), '019e43f9-4135-79ac-8741-551fa0b99f6d', '019e43f9-4135-79ac-8741-551fa0b99f6d')
on conflict do nothing;

insert into sys_org(sys_org_id, parent_org_id, code, name, sys_orgtype_id, search_path, is_legal_entity,
                    is_active, created_at, updated_at, created_by, updated_by)
select uuidv7(), null, 'DEFAULT', 'Default Organization', sys_orgtype_id, 'DEFAULT', true,
       true, now(), now(), '019e43f9-4135-79ac-8741-551fa0b99f6d', '019e43f9-4135-79ac-8741-551fa0b99f6d'
from sys_orgtype
where code = 'DEFAULT'
on conflict do nothing;

insert into sys_segment(sys_segment_id, parent_segment_id, code, name, search_path, is_active,
                        created_at, updated_at, created_by, updated_by)
select uuidv7(), null, 'DEFAULT', 'Default Segment', 'DEFAULT', true,
       now(), now(), '019e43f9-4135-79ac-8741-551fa0b99f6d', '019e43f9-4135-79ac-8741-551fa0b99f6d'
where not exists (select 1 from sys_segment where code = 'DEFAULT')
on conflict do nothing;

insert into sys_role(sys_role_id, code, name, description, is_active,
                     created_at, updated_at, created_by, updated_by)
select uuidv7(), 'ADMIN', 'Admin', 'Default administrator role', true,
       now(), now(), '019e43f9-4135-79ac-8741-551fa0b99f6d', '019e43f9-4135-79ac-8741-551fa0b99f6d'
where not exists (select 1 from sys_role where code = 'ADMIN')
on conflict do nothing;

insert into sys_position(sys_position_id, code, name, description, is_active,
                         created_at, updated_at, created_by, updated_by, sys_org_id)
select uuidv7(), 'ADMIN', 'Admin', 'Default administrator position', true,
       now(), now(), '019e43f9-4135-79ac-8741-551fa0b99f6d', '019e43f9-4135-79ac-8741-551fa0b99f6d', sys_org_id
from sys_org
where code = 'DEFAULT'
  and not exists (select 1 from sys_position where code = 'ADMIN')
on conflict do nothing;

insert into sys_user_position(sys_user_position_id, sys_user_id, sys_position_id, is_default, is_active,
                              created_at, updated_at, created_by, updated_by)
select uuidv7(), '019e43f9-4135-79ac-8741-551fa0b99f6d', p.sys_position_id, true, true,
       now(), now(), '019e43f9-4135-79ac-8741-551fa0b99f6d', '019e43f9-4135-79ac-8741-551fa0b99f6d'
from sys_position p
where p.code = 'ADMIN'
  and not exists (
      select 1
      from sys_user_position
      where sys_user_id = '019e43f9-4135-79ac-8741-551fa0b99f6d'
        and sys_position_id = p.sys_position_id
  )
on conflict do nothing;

--insert into sys_position_role_scope(sys_position_role_scope_id, sys_position_id, sys_role_id, org_scope, sys_org_id,
--                                    segment_scope, sys_segment_id, is_active, created_at, updated_at, created_by, updated_by)
--select uuidv7(), p.sys_position_id, r.sys_role_id, 'ALL', null,
--       'ALL', null, true, now(), now(), '019e43f9-4135-79ac-8741-551fa0b99f6d', '019e43f9-4135-79ac-8741-551fa0b99f6d'
--from sys_position p
--join sys_role r on r.code = 'ADMIN'
--where p.code = 'ADMIN'
--  and not exists (
--      select 1
--      from sys_position_role_scope scope
--      where scope.sys_position_id = p.sys_position_id
--        and scope.sys_role_id = r.sys_role_id
--        and scope.org_scope = 'ALL'
--        and scope.segment_scope = 'ALL'
--  )
--on conflict do nothing;
