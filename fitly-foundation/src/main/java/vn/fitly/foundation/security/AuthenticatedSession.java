package vn.fitly.foundation.security;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class AuthenticatedSession {

    private UUID sessionId;

    private UUID tenantId;

    private UUID userId;

    private String username;

    private UUID positionId;

    private List<UUID> roleIds;

    private String language;

    private String timezone;

    private String tenantServiceName;

    private boolean active;

    private LocalDateTime expiresAt;

    private LocalDateTime revokedAt;

    public boolean isValid(LocalDateTime now) {
        return active && revokedAt == null && expiresAt != null && expiresAt.isAfter(now);
    }

    public UUID getSessionId() {
        return sessionId;
    }

    public void setSessionId(UUID sessionId) {
        this.sessionId = sessionId;
    }

    public UUID getTenantId() {
        return tenantId;
    }

    public void setTenantId(UUID tenantId) {
        this.tenantId = tenantId;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public UUID getPositionId() {
        return positionId;
    }

    public void setPositionId(UUID positionId) {
        this.positionId = positionId;
    }

    public List<UUID> getRoleIds() {
        return roleIds;
    }

    public void setRoleIds(List<UUID> roleIds) {
        this.roleIds = roleIds;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getTimezone() {
        return timezone;
    }

    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }

    public String getTenantServiceName() {
        return tenantServiceName;
    }

    public void setTenantServiceName(String tenantServiceName) {
        this.tenantServiceName = tenantServiceName;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public LocalDateTime getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(LocalDateTime expiresAt) {
        this.expiresAt = expiresAt;
    }

    public LocalDateTime getRevokedAt() {
        return revokedAt;
    }

    public void setRevokedAt(LocalDateTime revokedAt) {
        this.revokedAt = revokedAt;
    }
}
