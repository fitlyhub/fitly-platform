/**
 * Project: Fitly Platform Author:  fitly.zero Date:    May 8, 2026 Time:    11:26:01 AM * Copyright (c) 2026
 * fitly.zero. All rights reserved. Licensed under the Apache License 2.0.
 */
package vn.fitly.common.exception;

/**
 *
 */
public class FitlyRuntimeException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private final int status;

    private final String errorCode;

    public FitlyRuntimeException(ErrorStatus status, ErrorCode errorCode, String message) {
        this(status, errorCode.name(), message);
    }

    public FitlyRuntimeException(ErrorStatus status, String errorCode, String message) {
        super(message, null, true, false);
        this.status = status.getStatus();
        this.errorCode = errorCode;
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
