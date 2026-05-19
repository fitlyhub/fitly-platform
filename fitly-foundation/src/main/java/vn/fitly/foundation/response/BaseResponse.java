/**

Project: Fitly Flatform

Author:  fitly.zero

Copyright (c) 2026 fitly.zero. All rights reserved.

Licensed under the Apache License 2.0.
*/
package vn.fitly.foundation.response;

/**
 * 
 */
public class BaseResponse<T> {

    private final boolean success;

    private final String message;

    private final T data;

    private BaseResponse(boolean success, String message, T data) {
        this.success = success;
        this.message = message;
        this.data = data;
    }

    /**
     * @return the isSuccess
     */
    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    public T getData() {
        return data;
    }

    public static <T> BaseResponse<T> success(T data) {
        return new BaseResponse<T>(true, null, data);
    }

    public static <T> BaseResponse<T> error(String message) {
        return new BaseResponse<T>(false, message, null);
    }

}
