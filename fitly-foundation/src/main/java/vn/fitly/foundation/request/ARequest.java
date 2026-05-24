/**
 * Project: Fitly Platform
 * Author:  fitly.zero
 * Date:    May 18, 2026
 * Time:    3:42:05 PM
 * * Copyright (c) 2026 fitly.zero. All rights reserved.
 * Licensed under the Apache License 2.0.
 */
package vn.fitly.foundation.request;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 
 */
public class ARequest {

    @JsonProperty("language_code")
    private String languageCode;

    @JsonProperty("token")
    private String token;

    @JsonProperty("trace_id")
    private String traceId;

    /**
     * @return the languageCode
     */
    public String getLanguageCode() {
        return languageCode;
    }

    /**
     * @param languageCode the languageCode to set
     */
    public void setLanguageCode(String languageCode) {
        this.languageCode = languageCode;
    }

    /**
     * @return the token
     */
    public String getToken() {
        return token;
    }

    /**
     * @param token the token to set
     */
    public void setToken(String token) {
        this.token = token;
    }

    public String getTraceId() {
        return traceId;
    }

    public void setTraceId(String traceId) {
        this.traceId = traceId;
    }

}
