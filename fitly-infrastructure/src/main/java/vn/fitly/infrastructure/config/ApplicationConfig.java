/**
 * Project: Fitly Platform
 * Author:  fitly.zero
 * Date:    May 14, 2026
 * Time:    9:46:03 AM
 * * Copyright (c) 2026 fitly.zero. All rights reserved.
 * Licensed under the Apache License 2.0.
 */
package vn.fitly.infrastructure.config;

import vn.fitly.infrastructure.datasource.FitlyDbType;
import vn.fitly.infrastructure.dto.DatasourceInfo;
import vn.fitly.infrastructure.spring.FitlyDatasourceConfig;
import vn.fitly.infrastructure.spring.FitlySessionConfig;

/**
 * 
 */
public class ApplicationConfig {

    private static DatasourceInfo DATASOURCE;

    private static FitlySessionConfig SESSION_CONFIG;

    private ApplicationConfig() {
    }

    public static void init(FitlyDatasourceConfig config, FitlySessionConfig sessionConfig) {
        DATASOURCE = new DatasourceInfo();
        DATASOURCE.initMasterDatasource(config);
        SESSION_CONFIG = sessionConfig;
    }

    public static DatasourceInfo getDatasource() {
        return DATASOURCE;
    }
    
    public static FitlyDbType getDatabaseType() {
        return DATASOURCE.getType();
    }

    public static FitlySessionConfig getSessionConfig() {
        return SESSION_CONFIG;
    }
 
}
