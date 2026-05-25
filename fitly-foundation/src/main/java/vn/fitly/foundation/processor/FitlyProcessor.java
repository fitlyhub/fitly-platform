/**
 * Project: Fitly Platform
 * Author:  fitly.zero
 * Date:    May 23, 2026
 * Time:    6:15:02 PM
 * * Copyright (c) 2026 fitly.zero. All rights reserved.
 * Licensed under the Apache License 2.0.
 */
package vn.fitly.foundation.processor;

import vn.fitly.common.exception.FitlyBussinessException;
import vn.fitly.foundation.context.Ctx;
import vn.fitly.foundation.context.UserPrincipal;

/**
 * 
 */
public abstract class FitlyProcessor<RQ, RP> extends BaseProcessor<RQ, RP> {

    public FitlyProcessor(RQ request) {
        super(request);
    }
    
    @Override
    public RP process() {
        return super.process();
    }
    
    private UserPrincipal authentication() {
        String sessionId = Ctx.http().getCookie("FITLY_SESSION");
        if(sessionId == null) {
            throw new FitlyBussinessException(null, SE);
        }
        
        
    }

}
