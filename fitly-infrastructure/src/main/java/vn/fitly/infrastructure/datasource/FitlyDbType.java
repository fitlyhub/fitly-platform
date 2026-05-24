/**
 * Project: Fitly Platform
 * Author:  fitly.zero
 * Date:    May 11, 2026
 * Time:    1:34:33 PM
 * * Copyright (c) 2026 fitly.zero. All rights reserved.
 * Licensed under the Apache License 2.0.
 */
package vn.fitly.infrastructure.datasource;

/**
 * 
 */
public enum FitlyDbType {

    POSTGRES,
    SQLite,

    ;

    public static FitlyDbType getType(String str) {
        if (str == null) {
            return null;
        }

        for (FitlyDbType type : FitlyDbType.values()) {
            if (type.name().equalsIgnoreCase(str)) {
                return type;
            }
        }

        return null;
    }

}
