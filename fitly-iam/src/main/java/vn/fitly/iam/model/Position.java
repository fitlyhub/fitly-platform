/**
 * Project: Fitly Platform
 * Author:  fitly.zero
 * Date:    May 19, 2026
 * Time:    3:18:56 PM
 * * Copyright (c) 2026 fitly.zero. All rights reserved.
 * Licensed under the Apache License 2.0.
 */
package vn.fitly.iam.model;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 */
public class Position {
    
    
    private String positionId;
    
    private String code;
    
    private String name;
    
    private int orgId;
    
    private List<DataScope> dataScopeList = new ArrayList<>();

    /**
     * @return the positionId
     */
    public String getPositionId() {
        return positionId;
    }

    /**
     * @param positionId the positionId to set
     */
    public void setPositionId(String positionId) {
        this.positionId = positionId;
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
     * @return the orgId
     */
    public int getOrgId() {
        return orgId;
    }

    /**
     * @param orgId the orgId to set
     */
    public void setOrgId(int orgId) {
        this.orgId = orgId;
    }

    /**
     * @return the dataScopeList
     */
    public List<DataScope> getDataScopeList() {
        return dataScopeList;
    }

    /**
     * @param dataScopeList the dataScopeList to set
     */
    public void setDataScopeList(List<DataScope> dataScopeList) {
        this.dataScopeList = dataScopeList;
    }
    
    

}
