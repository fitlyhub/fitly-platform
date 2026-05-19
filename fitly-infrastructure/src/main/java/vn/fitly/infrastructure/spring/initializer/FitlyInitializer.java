/**
 * Project: Fitly Platform
 * Author:  fitly.zero
 * Date:    May 11, 2026
 * Time:    3:31:21 PM
 * * Copyright (c) 2026 fitly.zero. All rights reserved.
 * Licensed under the Apache License 2.0.
 */
package vn.fitly.infrastructure.spring.initializer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import jakarta.annotation.PreDestroy;
import vn.fitly.infrastructure.config.ApplicationConfig;
import vn.fitly.infrastructure.datasource.tenant.DBProvider;

/**
 * 
 */
@Component
public class FitlyInitializer {

    @Autowired
    public void initApplicationConfig(FitlyDatasourceConfig config) {
        ApplicationConfig.init(config);
    }

    @PreDestroy
    public void preDestroy() {
        DBProvider.shutdown();
    }
}
