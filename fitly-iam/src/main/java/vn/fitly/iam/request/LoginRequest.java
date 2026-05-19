/**
 * Project: Fitly Platform
 * Author:  fitly.zero
 * Date:    May 10, 2026
 * Time:    4:50:11 PM
 * * Copyright (c) 2026 fitly.zero. All rights reserved.
 * Licensed under the Apache License 2.0.
 */
package vn.fitly.iam.request;

import vn.fitly.foundation.request.ARequest;

/**
 * 
 */
public class LoginRequest extends ARequest{

    private String username;

    private String password;

    /**
     * @return the username
     */
    public String getUsername() {
        return username;
    }

    /**
     * @param username the username to set
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * @param password the password to set
     */
    public void setPassword(String password) {
        this.password = password;
    }

   
}
