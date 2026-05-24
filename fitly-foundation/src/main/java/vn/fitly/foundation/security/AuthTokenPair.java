/**
 * Project: Fitly Platform
 * Author:  fitly.zero
 * Date:    May 22, 2026
 * Time:    11:45:00 AM
 * Copyright (c) 2026 fitly.zero. All rights reserved.
 * Licensed under the Apache License 2.0.
 */
package vn.fitly.foundation.security;

/**
 * Access token, refresh token, and session id issued after authentication.
 */
public class AuthTokenPair {

    private final String sessionId;

    private final String accessToken;

    private final String refreshToken;

    public AuthTokenPair(String sessionId, String accessToken, String refreshToken) {
        this.sessionId = sessionId;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }

    public String getSessionId() {
        return sessionId;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }
}
