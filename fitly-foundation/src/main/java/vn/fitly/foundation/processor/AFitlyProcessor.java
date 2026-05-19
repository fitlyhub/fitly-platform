/**
 * Project: Fitly Platform
 * Author:  fitly.zero
 * Date:    May 11, 2026
 * Time:    11:34:13 AM
 * * Copyright (c) 2026 fitly.zero. All rights reserved.
 * Licensed under the Apache License 2.0.
 */
package vn.fitly.foundation.processor;

import vn.fitly.common.exception.FitlyRuntimeException;

/**
 * 
 */
public abstract class AFitlyProcessor<RQ, RP> {

    protected final RQ request;

    protected abstract void validate() throws Exception;

    protected abstract RP processInternal() throws Exception;

    public AFitlyProcessor(RQ request) {
        this.request = request;
    }

    public final RP process() throws Exception {

        validate();

        return processInternal();

    }

    protected RQ getRequest() {
        return this.request;
    }

}
