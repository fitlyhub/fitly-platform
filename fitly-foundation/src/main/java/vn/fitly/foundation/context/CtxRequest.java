/**
 * Project: Fitly Platform
 * Author:  fitly.zero
 * Date:    May 10, 2026
 * Time:    9:25:27 PM
 * * Copyright (c) 2026 fitly.zero. All rights reserved.
 * Licensed under the Apache License 2.0.
 */
package vn.fitly.foundation.context;

import java.sql.Connection;

/**
 * 
 */
public class CtxRequest {

    protected final static ScopedValue<Ctx> CTX = ScopedValue.newInstance();

    public static Ctx get() {

        if (CTX.isBound()) {
            return CTX.get();
        }

        return null;

    }

    public static Connection getConnection() throws Exception {
        Ctx ctx = get();
        if (ctx == null) {
            return null;
        }

        return ctx.getConnection();
    }

    public static UserPrincipal getUser() {

        Ctx ctx = get();
        if (ctx == null) {
            return null;
        }

        return ctx.getUser();
    }
}
