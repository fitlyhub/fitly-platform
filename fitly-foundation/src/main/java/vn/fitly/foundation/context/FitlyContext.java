/**
 * Project: Fitly Platform
 * Author:  fitly.zero
 * Date:    May 23, 2026
 * Time:    12:33:19 PM
 * * Copyright (c) 2026 fitly.zero. All rights reserved.
 * Licensed under the Apache License 2.0.
 */
package vn.fitly.foundation.context;

/**
 * 
 */
public class FitlyContext {

    private final HttpContext httpCtx;

    private final SessionContext sessionCtx;
    
    public FitlyContext(HttpContext httpCtx) {
        this.httpCtx = httpCtx;
        this.sessionCtx = null;
    }

    public FitlyContext(HttpContext httpCtx, SessionContext sessionCtx) {
        this.httpCtx = httpCtx;
        this.sessionCtx = sessionCtx;
    }

    public HttpContext http() {
        return httpCtx;
    }

    public SessionContext session() {
        return sessionCtx;
    }

}
