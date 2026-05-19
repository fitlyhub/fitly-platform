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
    
    private UUID defaultOrganizationId;

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public UUID getDefaultOrganizationId() {
        return defaultOrganizationId;
    }

    public void setDefaultOrganizationId(UUID defaultOrganizationId) {
        this.defaultOrganizationId = defaultOrganizationId;
    }

    
}
