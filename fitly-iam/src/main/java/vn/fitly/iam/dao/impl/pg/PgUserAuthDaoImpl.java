package vn.fitly.iam.dao.impl.pg;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import vn.fitly.foundation.helper.DbHelper;
import vn.fitly.iam.dao.UserAuthDao;
import vn.fitly.iam.model.Position;
import vn.fitly.iam.model.TenantProfile;
import vn.fitly.iam.model.User;

public class PgUserAuthDaoImpl implements UserAuthDao {

    @Override
    public TenantProfile getTenantById(UUID tenantId) throws Exception {
        String sql = """
                select sys_tenant_profile_id, code, default_language, timezone
                from sys_tenant_profile
                where sys_tenant_profile_id = ?
                """;

        try (PreparedStatement ps = DbHelper.preparedStatement(sql, tenantId);
                ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return mapTenant(rs);
            }
        }

        return null;
    }

    @Override
    public TenantProfile getTenantByCode(String tenantCode) throws Exception {
        String sql = """
                select sys_tenant_profile_id, code, default_language, timezone
                from sys_tenant_profile
                where code = ?
                """;

        try (PreparedStatement ps = DbHelper.preparedStatement(sql, tenantCode);
                ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return mapTenant(rs);
            }
        }

        return null;
    }

    @Override
    public User getActiveUserByUsername(String username) throws Exception {
        String sql = """
                select *
                from sys_user
                where username = ?
                  and is_active = true
                  and is_locked = false
                """;

        try (PreparedStatement ps = DbHelper.preparedStatement(sql, username);
                ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return mapUser(rs);
            }
        }

        return null;
    }

    @Override
    public Position getDefaultPosition(UUID userId) throws Exception {
        String sql = """
                select p.*
                from sys_user_position up
                join sys_position p on p.sys_position_id = up.sys_position_id
                where up.sys_user_id = ?
                  and up.is_active = true
                  and p.is_active = true
                order by up.is_default desc, up.created_at asc
                limit 1
                """;

        try (PreparedStatement ps = DbHelper.preparedStatement(sql, userId);
                ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                Position position = new Position();
                position.setPositionId(rs.getString("sys_position_id"));
                position.setCode(rs.getString("code"));
                position.setName(rs.getString("name"));
                position.setOrgId(rs.getString("sys_org_id"));
                return position;
            }
        }

        return null;
    }

    @Override
    public List<UUID> getRoleIdsByPosition(UUID positionId) throws Exception {
        String sql = """
                select distinct prs.sys_role_id
                from sys_position_role_scope prs
                join sys_role r on r.sys_role_id = prs.sys_role_id
                where prs.sys_position_id = ?
                  and prs.is_active = true
                  and r.is_active = true
                order by prs.sys_role_id
                """;

        try (PreparedStatement ps = DbHelper.preparedStatement(sql, positionId);
                ResultSet rs = ps.executeQuery()) {
            List<UUID> roleIds = new ArrayList<>();
            while (rs.next()) {
                roleIds.add(UUID.fromString(rs.getString("sys_role_id")));
            }

            return roleIds;
        }
    }

    private TenantProfile mapTenant(ResultSet rs) throws Exception {
        TenantProfile tenant = new TenantProfile();
        tenant.setTenantId(UUID.fromString(rs.getString("sys_tenant_profile_id")));
        tenant.setCode(rs.getString("code"));
        tenant.setDefaultLanguage(rs.getString("default_language"));
        tenant.setTimezone(rs.getString("timezone"));
        return tenant;
    }

    private User mapUser(ResultSet rs) throws Exception {
        User user = new User();
        user.setUserId(UUID.fromString(rs.getString("sys_user_id")));
        user.setUsername(rs.getString("username"));
        user.setPassword(rs.getString("password"));
        user.setAvatarUrl(rs.getString("avatar_url"));
        user.setFirstName(rs.getString("first_name"));
        user.setLastName(rs.getString("last_name"));
        user.setFullName(rs.getString("full_name"));
        user.setActive(rs.getBoolean("is_active"));
        return user;
    }
}
