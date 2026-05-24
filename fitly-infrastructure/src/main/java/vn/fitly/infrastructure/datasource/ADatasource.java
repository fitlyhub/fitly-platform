/**
 * Project: Fitly Platform
 * Author:  fitly.zero
 * Date:    May 7, 2026
 * Time:    11:36:04 AM
 * * Copyright (c) 2026 fitly.zero. All rights reserved.
 * Licensed under the Apache License 2.0.
 */
package vn.fitly.infrastructure.datasource;

import vn.fitly.infrastructure.dto.DatasourceInfo;

/**
 * 
 */
public abstract class ADatasource<K> {

    private static final long MAX_TIME_UNUSED_DATASOURCE = 12 * 60 * 60 * 1000L; // 12 hours

    protected abstract K getConnectionFromDatasource();

    protected abstract void initDatasource(DatasourceInfo datasourceInfo);

    public abstract void closeDatasource();

    private volatile long lastUsedTime = System.currentTimeMillis();

    public ADatasource(DatasourceInfo datasourceInfo) {
        initDatasource(datasourceInfo);
    }

    public K getConnection() {
        lastUsedTime = System.currentTimeMillis();
        return getConnectionFromDatasource();
    }

    public boolean isExpired() {

        long delta = System.currentTimeMillis() - lastUsedTime;
        return MAX_TIME_UNUSED_DATASOURCE <= delta;

    }

}
