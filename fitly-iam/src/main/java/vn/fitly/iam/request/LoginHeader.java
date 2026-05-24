/**
 * Project: Fitly Platform
 * Author:  fitly.zero
 * Date:    May 22, 2026
 * Time:    1:49:18 PM
 * * Copyright (c) 2026 fitly.zero. All rights reserved.
 * Licensed under the Apache License 2.0.
 */
package vn.fitly.iam.request;

/**
 * 
 */
public class LoginHeader {

    private String userAgent;

    private String ip;

    public LoginHeader(String userAgent, String ip) {
        this.userAgent = userAgent;
        this.ip = ip;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public String getIp() {
        return ip;
    }

}
