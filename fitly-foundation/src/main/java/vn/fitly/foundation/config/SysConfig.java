/**
 * Project: Fitly Platform
 * Author:  fitly.zero
 * Date:    May 13, 2026
 * Time:    7:11:23 PM
 * * Copyright (c) 2026 fitly.zero. All rights reserved.
 * Licensed under the Apache License 2.0.
 */
package vn.fitly.foundation.config;

import java.math.BigDecimal;

/**
 * 
 */
public class SysConfig {

    private static String getConfigAsString(String configName) {
        return SysConfigLoader.getConfigValue(configName);
    }

    private static String getConfigAsString(String configName, String defaultVal) {
        String val = getConfigAsString(configName);
        if (val == null) {
            return defaultVal;
        }

        return val;
    }

    private static BigDecimal getConfigAsBigDecimal(String configName, BigDecimal defaultVal) {
        String val = getConfigAsString(configName);
        if (val == null) {
            return defaultVal;
        }

        return new BigDecimal(val);
    }

    private static Integer getConfigAsInteger(String configName, Integer defaultVal) {
        String val = getConfigAsString(configName);
        if (val == null) {
            return defaultVal;
        }

        return Integer.parseInt(val);
    }

    private static Boolean getConfigAsBoolean(String configName, boolean defaultVal) {
        String val = getConfigAsString(configName);
        if (val == null) {
            return defaultVal;
        }

        if ("Y".equalsIgnoreCase(val)) {
            return true;
        }

        return Boolean.valueOf(val);

    }

}
