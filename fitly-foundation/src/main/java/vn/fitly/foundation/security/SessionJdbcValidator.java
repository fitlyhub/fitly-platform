package vn.fitly.foundation.security;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import vn.fitly.infrastructure.datasource.tenant.DBProvider;

public final class SessionJdbcValidator {

    private SessionJdbcValidator() {
    }

    public static AuthenticatedSession findValidSession(String sessionTokenHash) throws Exception {
        if (sessionTokenHash == null || sessionTokenHash.isBlank()) {
            return null;
        }

        AuthenticatedSession cached = SessionCache.get(sessionTokenHash);
        LocalDateTime now = LocalDateTime.now();
        if (cached != null && cached.isValid(now)) {
            return cached;
        }

        SessionCache.remove(sessionTokenHash);
        AuthenticatedSession session = findByHash(sessionTokenHash);
        if (session == null || !session.isValid(now)) {
            return null;
        }

        updateLastAccess(session.getSessionId());
        SessionCache.put(sessionTokenHash, session);
        return session;
    }

    private static AuthenticatedSession findByHash(String sessionTokenHash) throws Exception {
        String sql = """
                select s.auth_session_id,
                       s.tenant_id,
                       s.sys_user_id,
                       u.username,
                       s.sys_position_id,
                       s.is_active,
                       s.expires_at,
                       s.revoked_at,
                       coalesce(tp.default_language, 'vi_VN') as default_language,
                       coalesce(tp.timezone, 'Asia/Ho_Chi_Minh') as timezone
                from auth_session s
                join sys_user u on u.sys_user_id = s.sys_user_id
                left join sys_tenant_profile tp on tp.sys_tenant_profile_id = s.tenant_id
                where s.session_token_hash = ?
                """;

        try (Connection conn = DBProvider.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, sessionTokenHash);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) {
                    return null;
                }

                AuthenticatedSession session = new AuthenticatedSession();
                session.setSessionId(readUuid(rs, "auth_session_id"));
                session.setTenantId(readUuid(rs, "tenant_id"));
                session.setUserId(readUuid(rs, "sys_user_id"));
                session.setUsername(rs.getString("username"));
                session.setPositionId(readUuid(rs, "sys_position_id"));
                session.setActive(rs.getBoolean("is_active"));
                session.setExpiresAt(readLocalDateTime(rs, "expires_at"));
                session.setRevokedAt(readLocalDateTime(rs, "revoked_at"));
                session.setLanguage(rs.getString("default_language"));
                session.setTimezone(rs.getString("timezone"));
                session.setRoleIds(findRoleIds(conn, session.getPositionId()));
                return session;
            }
        }
    }

    private static List<UUID> findRoleIds(Connection conn, UUID positionId) throws Exception {
        List<UUID> roleIds = new ArrayList<>();
        if (positionId == null) {
            return roleIds;
        }

        String sql = """
                select distinct prs.sys_role_id
                from sys_position_role_scope prs
                join sys_role r on r.sys_role_id = prs.sys_role_id
                where prs.sys_position_id = ?
                  and prs.is_active = true
                  and r.is_active = true
                order by prs.sys_role_id
                """;
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setObject(1, positionId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    roleIds.add(readUuid(rs, "sys_role_id"));
                }
            }
        }

        return roleIds;
    }

    private static void updateLastAccess(UUID sessionId) throws Exception {
        if (sessionId == null) {
            return;
        }

        String sql = "update auth_session set last_access_at = now(), updated_at = now() where auth_session_id = ?";
        try (Connection conn = DBProvider.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setObject(1, sessionId);
            ps.executeUpdate();
        }
    }

    private static UUID readUuid(ResultSet rs, String columnName) throws Exception {
        Object value = rs.getObject(columnName);
        if (value == null) {
            return null;
        }

        if (value instanceof UUID) {
            return (UUID) value;
        }

        return UUID.fromString(value.toString());
    }

    private static LocalDateTime readLocalDateTime(ResultSet rs, String columnName) throws Exception {
        Object value = rs.getObject(columnName);
        if (value == null) {
            return null;
        }

        if (value instanceof LocalDateTime) {
            return (LocalDateTime) value;
        }

        return rs.getTimestamp(columnName).toLocalDateTime();
    }
}
