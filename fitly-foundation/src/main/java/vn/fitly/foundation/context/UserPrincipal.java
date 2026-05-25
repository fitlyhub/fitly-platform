/**
 * Project: Fitly Platform
 * Author:  fitly.zero
 * Date:    Apr 30, 2026
 * Time:    9:33:44 AM
 * * Copyright (c) 2026 fitlyzero. All rights reserved.
 * Licensed under the Apache License 2.0.
 */
package vn.fitly.foundation.context;

import java.util.UUID;

/**
 * 
 */
public class UserPrincipal {

    private UUID userId;

    private String username;

    private UUID positionId;

    public UserPrincipal(UUID userId, String username, UUID positionId) {
        this.userId = userId;
        this.username = username;
        this.positionId = positionId;
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

}
