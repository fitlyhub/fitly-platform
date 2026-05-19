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
public abstract class ARequest {

    @JsonProperty("language_code")
    private String languageCode;

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

}
