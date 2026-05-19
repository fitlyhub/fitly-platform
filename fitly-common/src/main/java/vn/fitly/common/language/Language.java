/**
 * Project: Fitly Platform
 * Author:  fitly.zero
 * Date:    May 18, 2026
 * Time:    1:39:47 PM
 * * Copyright (c) 2026 fitly.zero. All rights reserved.
 * Licensed under the Apache License 2.0.
 */
package vn.fitly.common.language;

/**
 * 
 */
public enum Language {

    VIETNAMESE("vi_VN"),
    ENGLISH("en_US")

    ;

    private final String code;

    private Language(String code) {
        this.code = code;
    }

    public static Language getLanguage(String code) {

        for (Language lang : Language.values()) {
            if (lang.code.equals(code)) {
                return lang;
            }

        }

        // default
        return ENGLISH;
    }

}
