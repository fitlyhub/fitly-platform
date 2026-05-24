/**
 * Project: Fitly Platform
 * Author:  fitly.zero
 * Date:    May 8, 2026
 * Time:    11:26:01 AM
 * * Copyright (c) 2026 fitly.zero. All rights reserved.
 * Licensed under the Apache License 2.0.
 */
package vn.fitly.common.exception;

/**
 * 
 */
public class FitlyRuntimeException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private final int status;

    private final String errorCode;

    public FitlyRuntimeException(ErrorStatus status, ErrorCode errorCode) {
        this(status, errorCode, null);
    }

    public FitlyRuntimeException(ErrorStatus status, String errorCode) {
        this(status, errorCode, null);
    }

    public FitlyRuntimeException(ErrorStatus status, ErrorCode errorCode, Exception cause) {

        this(status, errorCode.name(), cause);
    }

    public FitlyRuntimeException(ErrorStatus status, String errorCode, Exception cause) {

        super(errorCode, cause, true, cause != null);

        this.status = status.getStatus();
        this.errorCode = errorCode;
    }

    /**
     * @return the serialversionuid
     */
    public static long getSerialversionuid() {
        return serialVersionUID;
    }

    /**
     * @return the status
     */
    public int getStatus() {
        return status;
    }

    /**
     * @return the errorCode
     */
    public String getErrorCode() {
        return errorCode;
    }

}
