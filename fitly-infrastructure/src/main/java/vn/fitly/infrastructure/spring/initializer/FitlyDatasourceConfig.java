/**
 * Project: Fitly Platform
 * Author:  fitly.zero
 * Date:    May 7, 2026
 * Time:    11:26:42 AM
 * * Copyright (c) 2026 fitly.zero. All rights reserved.
 * Licensed under the Apache License 2.0.
 */
package vn.fitly.infrastructure.spring.initializer;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 
 */
@ConfigurationProperties(prefix = "fitly.datasource")
public record FitlyDatasourceConfig(
        String host,
        int port,
        String username,
        String password,
        String databaseName,
        String schema,
        String type) {
}
