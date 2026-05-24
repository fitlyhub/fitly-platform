/**
 * Project: Fitly Platform
 * Author:  fitly.zero
 * Date:    May 23, 2026
 * Time:    11:52:35 AM
 * * Copyright (c) 2026 fitly.zero. All rights reserved.
 * Licensed under the Apache License 2.0.
 */
package vn.fitly.iam.request;

import com.fasterxml.jackson.annotation.JsonProperty;

import vn.fitly.foundation.request.ARequest;

/**
 * 
 */
public class CompleteLoginRequest extends ARequest {

    @JsonProperty("position_id")
    private String positionId;

    public String getPositionId() {
        return positionId;
    }

    public void setPositionId(String positionId) {
        this.positionId = positionId;
    }

}
