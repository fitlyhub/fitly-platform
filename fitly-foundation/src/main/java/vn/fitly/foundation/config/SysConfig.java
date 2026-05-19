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
    
    private static String getConfigAsString(String configName, String defaultVal) {
        return new SysConfigLoader<>(configName, String.class).getWithDefault(defaultVal);
    }
    
    private static BigDecimal getConfigAsBigDecimal(String configName, BigDecimal defaultVal) {
        return new SysConfigLoader<>(configName, BigDecimal.class).getWithDefault(defaultVal);
    }
    
    private static Integer getConfigAsInteger(String configName, Integer defaultVal) {
        return new SysConfigLoader<>(configName, Integer.class).getWithDefault(defaultVal);
    }
    
    private static Boolean getConfigAsBoolean(String configName, boolean defaultVal) {
        return new SysConfigLoader<>(configName, Boolean.class).getWithDefault(defaultVal);
    }
    

}
