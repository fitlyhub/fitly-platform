/**
 * Project: Fitly Platform
 * Author:  fitly.zero
 * Date:    May 10, 2026
 * Time:    4:51:32 PM
 * * Copyright (c) 2026 fitly.zero. All rights reserved.
 * Licensed under the Apache License 2.0.
 */
package vn.fitly.iam.response;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonProperty;

import vn.fitly.iam.model.Position;

/**
 * 
 */
public class LoginResponse {

    @JsonProperty("user_id")
    private UUID userId;

    @JsonProperty("position")
    private List<Position> positionList;

    @JsonProperty("trace_id")
    private String traceId;

    @JsonProperty
    private boolean needSelectPosition;

    public LoginResponse(UUID userId, List<Position> positionList, String traceId) {
        this.userId = userId;
        this.positionList = positionList;
        this.traceId = traceId;
        this.needSelectPosition = positionList.size() > 1;
    }

    public LoginResponse(UUID userId, Position position, String traceId) {
        this.userId = userId;
        this.positionList = new ArrayList<>();
        this.positionList.add(position);
        this.traceId = traceId;
        this.needSelectPosition = false;
    }

    /**
     * @return the userId
     */
    public UUID getUserId() {
        return userId;
    }

    /**
     * @param userId the userId to set
     */
    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    /**
     * @return the positionList
     */
    public List<Position> getPositionList() {
        return positionList;
    }

    /**
     * @param positionList the positionList to set
     */
    public void setPositionList(List<Position> positionList) {
        this.positionList = positionList;
    }

    public String getTraceId() {
        return traceId;
    }

    public void setTraceId(String traceId) {
        this.traceId = traceId;
    }

    public boolean isNeedSelectPosition() {
        return needSelectPosition;
    }

    public void setNeedSelectPosition(boolean needSelectPosition) {
        this.needSelectPosition = needSelectPosition;
    }

}
