/**
 * Project: Fitly Platform
 * Author:  fitly.zero
 * Date:    May 11, 2026
 * Time:    11:34:13 AM
 * * Copyright (c) 2026 fitly.zero. All rights reserved.
 * Licensed under the Apache License 2.0.
 */
package vn.fitly.foundation.processor;

import vn.fitly.common.exception.ErrorStatus;
import vn.fitly.common.exception.FitlyRuntimeException;
import vn.fitly.common.language.DefaultSystemMessage;

/**
 * 
 */
public abstract class BaseProcessor<RQ, RP> implements IProcessor<RQ, RP> {

    protected abstract void validate() throws Exception;

    protected abstract RP doProcess() throws Exception;

    protected final RQ request;

    public BaseProcessor(RQ request) {
        this.request = request;
    }

    public RP process() {
        try {
            validate();
            return doProcess();

        } catch (FitlyRuntimeException e) {
            throw e;

        } catch (Exception e) {
            throw new FitlyRuntimeException(
                    ErrorStatus.INTERNAL_ERROR,
                    DefaultSystemMessage.INTERNAL_ERROR,
                    e);
        }
    }

    protected RQ getRequest() {
        return this.request;
    }

}
