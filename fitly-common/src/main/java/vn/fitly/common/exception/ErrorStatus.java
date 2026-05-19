/**
 * Project: Fitly Platform
 * Author:  fitly.zero
 * Date:    May 18, 2026
 * Time:    1:28:40 PM
 * * Copyright (c) 2026 fitly.zero. All rights reserved.
 * Licensed under the Apache License 2.0.
 */
package vn.fitly.common.exception;

/**
 * 
 */
public enum ErrorStatus {

    REQUEST_INVALID(400),

    UNAUTHORIZED(401),

    // role invalid
    FORBIDDEN(403),

    // use for data not found
    NOTFOUND(404),

    DATA_CONFLICT(409),

    RULE_EXCEPTION(422),

    INTERNAL_ERROR(500),

    ;

    private final int status;

    private ErrorStatus(int status) {
        this.status = status;
    }

    public int getStatus() {
        return status;
    }

}
