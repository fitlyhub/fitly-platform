/**
 * Project: Fitly Platform
 * Author:  fitly.zero
 * Date:    May 21, 2026
 * Time:    2:00:00 PM
 * Copyright (c) 2026 fitly.zero. All rights reserved.
 * Licensed under the Apache License 2.0.
 */
package vn.fitly.foundation.security;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonProperty;

import vn.fitly.common.json.JsonMapperBuilder;

/**
 * Claims extracted from a user access token.
 */
public class AccessToken {

    @JsonProperty("session_id")
    private String sessionId;

    @JsonProperty("user_id")
    private UUID userId;

    private String username;

    @JsonProperty("position_id")
    private UUID positionId;

    @JsonProperty("tenant_id")
    private UUID tenantId;

    public AccessToken() {
    }

    /**
     * @param userId
     * @param username
     * @param positionId
     * @param tenantId
     */
    public AccessToken(UUID userId, String username, UUID positionId, UUID tenantId) {
        this.userId = userId;
        this.username = username;
        this.positionId = positionId;
        this.tenantId = tenantId;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
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

    public String toString() {
        return JsonMapperBuilder.get().writeValueAsString(this);
    }

}
