/**
 * Project: Fitly Platform
 * Author:  fitly.zero
 * Date:    May 21, 2026
 * Time:    2:00:00 PM
 * Copyright (c) 2026 fitly.zero. All rights reserved.
 * Licensed under the Apache License 2.0.
 */
package vn.fitly.studio.dao.impl.pg;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import vn.fitly.foundation.dto.UserPrincipal;
import vn.fitly.foundation.helper.DbHelper;
import vn.fitly.studio.dao.MenuDao;
import vn.fitly.studio.dto.HeaderDto;
import vn.fitly.studio.dto.MenuDto;

/**
 * Postgres implementation for main display metadata.
 */
public class PgMenuDaoImpl implements MenuDao {

    @Override
    public UserPrincipal getUserPrincipal(UUID userId) throws Exception {

        String sql = """
                select u.sys_user_id,
                       u.username,
                       p.sys_position_id,
                       p.sys_org_id,
                       r.sys_role_id
                from sys_user u
                left join sys_user_position up
                       on up.sys_user_id = u.sys_user_id
                      and up.is_active = true
                left join sys_position p
                       on p.sys_position_id = up.sys_position_id
                      and p.is_active = true
                left join sys_position_role_scope prs
                       on prs.sys_position_id = p.sys_position_id
                      and prs.is_active = true
                left join sys_role r
                       on r.sys_role_id = prs.sys_role_id
                      and r.is_active = true
                where u.sys_user_id = ?
                  and u.is_active = true
                order by up.is_default desc nulls last,
                         up.created_at asc nulls last,
                         prs.created_at asc nulls last
                limit 1
                """;

        try (PreparedStatement ps = DbHelper.preparedStatement(sql, userId);
                ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                UserPrincipal principal = new UserPrincipal();
                principal.setUserId(UUID.fromString(rs.getString("sys_user_id")));
                principal.setUsername(rs.getString("username"));
                principal.setPositionId(readUuid(rs, "sys_position_id"));
                principal.setDefaultOrganizationId(readUuid(rs, "sys_org_id"));
                principal.setRoleId(readUuid(rs, "sys_role_id"));

                return principal;
            }
        }

        return null;
    }

    @Override
    public HeaderDto getUserHeaderDetails(UUID userId) throws Exception {

        String sql = """
                select u.sys_user_id,
                       u.username,
                       u.first_name,
                       u.last_name,
                       u.full_name,
                       u.email,
                       u.avatar_url,
                       p.sys_position_id,
                       p.code as position_code,
                       p.name as position_name,
                       r.sys_role_id,
                       r.code as role_code,
                       r.name as role_name,
                       o.sys_org_id,
                       o.code as org_code,
                       o.name as org_name
                from sys_user u
                left join sys_user_position up
                       on up.sys_user_id = u.sys_user_id
                      and up.is_active = true
                left join sys_position p
                       on p.sys_position_id = up.sys_position_id
                      and p.is_active = true
                left join sys_org o
                       on o.sys_org_id = p.sys_org_id
                      and o.is_active = true
                left join sys_position_role_scope prs
                       on prs.sys_position_id = p.sys_position_id
                      and prs.is_active = true
                left join sys_role r
                       on r.sys_role_id = prs.sys_role_id
                      and r.is_active = true
                where u.sys_user_id = ?
                  and u.is_active = true
                order by up.is_default desc nulls last,
                         up.created_at asc nulls last,
                         prs.created_at asc nulls last
                limit 1
                """;

        try (PreparedStatement ps = DbHelper.preparedStatement(sql, userId);
                ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                HeaderDto header = new HeaderDto();
                header.setUserId(readString(rs, "sys_user_id"));
                header.setUsername(rs.getString("username"));
                header.setFirstName(rs.getString("first_name"));
                header.setLastName(rs.getString("last_name"));
                header.setFullName(rs.getString("full_name"));
                header.setEmail(rs.getString("email"));
                header.setAvatarUrl(rs.getString("avatar_url"));
                header.setPositionId(readString(rs, "sys_position_id"));
                header.setPositionCode(rs.getString("position_code"));
                header.setPositionName(rs.getString("position_name"));
                header.setRoleId(readString(rs, "sys_role_id"));
                header.setRoleCode(rs.getString("role_code"));
                header.setRoleName(rs.getString("role_name"));
                header.setOrgId(readString(rs, "sys_org_id"));
                header.setOrgCode(rs.getString("org_code"));
                header.setOrgName(rs.getString("org_name"));

                return header;
            }
        }

        return null;
    }

    @Override
    public List<MenuDto> getAllActiveMenus() throws Exception {

        String sql = """
                select std_menu_id,
                       parent_menu_id,
                       code,
                       name,
                       description,
                       seq_no,
                       action_type,
                       std_page_id,
                       std_action_id
                from std_menu
                where is_active = true
                order by seq_no asc, name asc
                """;

        try (PreparedStatement ps = DbHelper.preparedStatement(sql);
                ResultSet rs = ps.executeQuery()) {

            List<MenuDto> menus = new ArrayList<>();
            while (rs.next()) {
                MenuDto menu = new MenuDto();
                menu.setMenuId(readString(rs, "std_menu_id"));
                menu.setParentMenuId(readString(rs, "parent_menu_id"));
                menu.setCode(rs.getString("code"));
                menu.setName(rs.getString("name"));
                menu.setDescription(rs.getString("description"));
                menu.setSeqNo(rs.getInt("seq_no"));
                menu.setActionType(rs.getString("action_type"));
                menu.setPageId(readString(rs, "std_page_id"));
                menu.setActionId(readString(rs, "std_action_id"));

                menus.add(menu);
            }

            return menus;
        }
    }

    @Override
    public Set<String> getPermittedMenuIdsByRole(UUID roleId) throws Exception {

        Set<String> permittedIds = new HashSet<>();
        if (roleId == null) {
            return permittedIds;
        }

        String sql = """
                select distinct m.std_menu_id
                from std_menu m
                where m.is_active = true
                  and (
                      exists (
                          select 1
                          from std_access a
                          where a.sys_role_id = ?
                            and a.access_type = 'MENU'
                            and a.record_id = m.std_menu_id
                            and a.is_permitted = true
                            and a.is_active = true
                      )
                      or exists (
                          select 1
                          from std_access a
                          where a.sys_role_id = ?
                            and a.access_type = 'PAGE'
                            and a.record_id = m.std_page_id
                            and a.is_permitted = true
                            and a.is_active = true
                      )
                      or exists (
                          select 1
                          from std_access a
                          where a.sys_role_id = ?
                            and a.access_type = 'ACTION'
                            and a.record_id = m.std_action_id
                            and a.is_permitted = true
                            and a.is_active = true
                      )
                  )
                """;

        try (PreparedStatement ps = DbHelper.preparedStatement(sql, roleId, roleId, roleId);
                ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                permittedIds.add(readString(rs, "std_menu_id"));
            }
        }

        return permittedIds;
    }

    private UUID readUuid(ResultSet rs, String columnName) throws Exception {
        String value = readString(rs, columnName);
        if (value == null) {
            return null;
        }

        return UUID.fromString(value);
    }

    private String readString(ResultSet rs, String columnName) throws Exception {
        Object value = rs.getObject(columnName);
        if (value == null) {
            return null;
        }

        return value.toString();
    }
}
