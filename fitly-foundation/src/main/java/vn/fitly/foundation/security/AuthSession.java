/**
 * Project: Fitly Platform
 * Author:  fitly.zero
 * Date:    May 22, 2026
 * Time:    11:45:00 AM
 * Copyright (c) 2026 fitly.zero. All rights reserved.
 * Licensed under the Apache License 2.0.
 */
package vn.fitly.foundation.security;

import java.time.Instant;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Server-side auth session used to revoke refresh tokens without invalidating
 * already-issued short-lived access tokens immediately.
 */
public class AuthSession {

    @JsonProperty("session_id")
    private String sessionId;

    @JsonProperty("refresh_token")
    private String refreshToken;

    @JsonProperty("expires_at")
    private Instant expiresAt;

    private boolean revoked;

    @JsonProperty("user_id")
    private UUID userId;

    private String username;

    @JsonProperty("position_id")
    private UUID positionId;

    @JsonProperty("tenant_id")
    private UUID tenantId;

    @JsonProperty("default_organization_id")
    private UUID defaultOrganizationId;

    @JsonProperty("role_id")
    private UUID roleId;

    public AuthSession() {
    }

    public AuthSession(String sessionId, String refreshToken, Instant expiresAt, AccessToken token) {
        this.sessionId = sessionId;
        this.refreshToken = refreshToken;
        this.expiresAt = expiresAt;
        this.userId = token.getUserId();
        this.username = token.getUsername();
        this.positionId = token.getPositionId();
        this.tenantId = token.getTenantId();
    }

    public AccessToken toAccessToken() {
        AccessToken token = new AccessToken(userId, username, positionId, tenantId);
        token.setSessionId(sessionId);
        return token;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public Instant getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(Instant expiresAt) {
        this.expiresAt = expiresAt;
    }

    public boolean isRevoked() {
        return revoked;
    }

    public void setRevoked(boolean revoked) {
        this.revoked = revoked;
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

    public UUID getTenantId() {
        return tenantId;
    }

    public void setTenantId(UUID tenantId) {
        this.tenantId = tenantId;
    }

    public UUID getDefaultOrganizationId() {
        return defaultOrganizationId;
    }

    public void setDefaultOrganizationId(UUID defaultOrganizationId) {
        this.defaultOrganizationId = defaultOrganizationId;
    }

    public UUID getRoleId() {
        return roleId;
    }

    public void setRoleId(UUID roleId) {
        this.roleId = roleId;
    }
}
