/**
 * Project: Fitly Platform
 * Author:  fitly.zero
 * Date:    May 18, 2026
 * Time:    1:47:29 PM
 * * Copyright (c) 2026 fitly.zero. All rights reserved.
 * Licensed under the Apache License 2.0.
 */
package vn.fitly.common.language;

/**
 * 
 */
public enum DefaultSystemMessage {

    REQUEST_INVALID("Request invalid", "Yêu cầu không hợp lệ"),
    INTERNAL_ERROR("Internal server error", "Lỗi hệ thống"),
    LOGIN_INVALID("Invalid username or password", "Tài khoản hoặc mật khẩu không đúng");
    ;

    // default support vietnamese and english

    private final String messageEng;

    private final String messageVi;

    /**
     * @param messageEng
     * @param messageVi
     */
    private DefaultSystemMessage(String messageEng, String messageVi) {
        this.messageEng = messageEng;
        this.messageVi = messageVi;
    }

    public String getMessage(String languageCode) {

        Language language = Language.getLanguage(languageCode);

        if (language == null) {
            return messageEng;
        }

        if (language == Language.VIETNAMESE) {
            return messageVi;
        }

        return messageEng;
    }

}
