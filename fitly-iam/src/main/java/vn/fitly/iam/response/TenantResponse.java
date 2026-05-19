/**
 * Project: Fitly Platform
 * Author:  fitly.zero
 * Date:    May 18, 2026
 * Time:    12:06:57 PM
 * * Copyright (c) 2026 fitly.zero. All rights reserved.
 * Licensed under the Apache License 2.0.
 */
package vn.fitly.iam.response;

import java.util.List;

/**
 * 
 */
public class TenantResponse {

    private String tenantId;

    private String code;

    private String name;

    private String domain;

    private String logoUrl;

    private String timezone;

    private List<String> languageCodeList;

    /**
     * @return the tenantId
     */
    public String getTenantId() {
        return tenantId;
    }

    /**
     * @param tenantId the tenantId to set
     */
    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    /**
     * @return the code
     */
    public String getCode() {
        return code;
    }

    /**
     * @param code the code to set
     */
    public void setCode(String code) {
        this.code = code;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the domain
     */
    public String getDomain() {
        return domain;
    }

    /**
     * @param domain the domain to set
     */
    public void setDomain(String domain) {
        this.domain = domain;
    }

    /**
     * @return the logoUrl
     */
    public String getLogoUrl() {
        return logoUrl;
    }

    /**
     * @param logoUrl the logoUrl to set
     */
    public void setLogoUrl(String logoUrl) {
        this.logoUrl = logoUrl;
    }

    /**
     * @return the timezone
     */
    public String getTimezone() {
        return timezone;
    }

    /**
     * @param timezone the timezone to set
     */
    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }

    /**
     * @return the languageCodeList
     */
    public List<String> getLanguageCodeList() {
        return languageCodeList;
    }

    /**
     * @param languageCodeList the languageCodeList to set
     */
    public void setLanguageCodeList(List<String> languageCodeList) {
        this.languageCodeList = languageCodeList;
    }
    
    

}
