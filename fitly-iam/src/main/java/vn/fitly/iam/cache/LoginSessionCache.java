/**
 * Project: Fitly Platform
 * Author:  fitly.zero
 * Date:    May 22, 2026
 * Time:    9:14:33 PM
 * * Copyright (c) 2026 fitly.zero. All rights reserved.
 * Licensed under the Apache License 2.0.
 */
package vn.fitly.iam.cache;

import vn.fitly.foundation.cache.ICacheEntry;
import vn.fitly.iam.response.LoginResponse;

/**
 * 
 */
public class LoginSessionCache implements ICacheEntry<LoginResponse> {

    private final String loginSession;

    public LoginSessionCache(String loginSession) {
        this.loginSession = loginSession;
    }

    @Override
    public String getKey() {
        return "login_session:" + loginSession;
    }

    @Override
    public long getTtlSeconds() {
        return 30;
    }

    @Override
    public Class<LoginResponse> getClazz() {
        return LoginResponse.class;
    }

}
