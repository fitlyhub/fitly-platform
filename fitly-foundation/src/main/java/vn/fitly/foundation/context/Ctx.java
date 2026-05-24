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

import vn.fitly.foundation.processor.IProcessor;

/**
 * 
 */
public class Ctx {

    private final static ScopedValue<FitlyContext> CTX = ScopedValue.newInstance();

    public static SessionContext get() {

        if (CTX.isBound()) {
            return CTX.get().session();
        }

        return null;

    }
    
    public static HttpContext http() {

        if (CTX.isBound()) {
            return CTX.get().http();
        }

        return null;

    }


    protected static ScopedValue<FitlyContext> getInstance() {
        return CTX;
    }

    public static Connection getConnection() throws Exception {
        SessionContext ctx = get();
        if (ctx == null) {
            return null;
        }

        return ctx.getConnection();
    }

    public static <RQ, RP> RP call(FitlyContext context, IProcessor<RQ, RP> processor) {
        return ScopedValue.where(CTX, context).call(processor::process);
    }

}
