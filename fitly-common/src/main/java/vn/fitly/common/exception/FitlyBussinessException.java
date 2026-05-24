/**
 * Project: Fitly Platform
 * Author:  fitly.zero
 * Date:    May 18, 2026
 * Time:    12:19:08 PM
 * * Copyright (c) 2026 fitly.zero. All rights reserved.
 * Licensed under the Apache License 2.0.
 */
package vn.fitly.common.exception;

/**
 * 
 */
public class FitlyBussinessException extends FitlyRuntimeException {

    private static final long serialVersionUID = 1L;

    /**
     * @param status
     * @param errorCode
     */
    public FitlyBussinessException(ErrorStatus status, String errorCode) {
        super(status, errorCode);
    }
    
    public FitlyBussinessException(ErrorStatus status, ErrorCode errorCode) {
        super(status, errorCode);
    }

}
