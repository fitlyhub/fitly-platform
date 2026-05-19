/**
 * Project: Fitly Platform
 * Author:  fitly.zero
 * Date:    May 7, 2026
 * Time:    8:05:29 PM
 * * Copyright (c) 2026 fitly.zero. All rights reserved.
 * Licensed under the Apache License 2.0.
 */
package vn.fitly.infrastructure.datasource.tenant;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import vn.fitly.common.exception.FitlyRuntimeException;
import vn.fitly.infrastructure.datasource.AFitlyDatasource;
import vn.fitly.infrastructure.dto.DatasourceInfo;

/**
 * 
 */
public abstract class AMultiDatasource<K> {

    private final Map<String, DatasourceInfo> routerMap = new ConcurrentHashMap<>();

    private final Map<String, AFitlyDatasource<K>> datasourceMap = new ConcurrentHashMap<>();

    protected abstract AFitlyDatasource<K> createDatasource(DatasourceInfo datasourceInfo);

    protected abstract void setSchema(K connection, String schema) throws FitlyRuntimeException;

    public K getConnectionWithServiceName(String serviceName) {

        DatasourceInfo datasourceInfo = routerMap.computeIfAbsent(serviceName, _ -> {
            return FitlyDatasource.loadDatasource(serviceName);
        });

        if (datasourceInfo == null) {
            return null;
        }

        AFitlyDatasource<K> datasource = datasourceMap.computeIfAbsent(
                datasourceInfo.getDatasourceKey(),
                _ -> {
                    return createDatasource(datasourceInfo);

                });

        K connection = datasource.getConnection();

        if (datasourceInfo.getSchema() != null) {
            setSchema(connection, datasourceInfo.getSchema());
        }

        return connection;
    }

    public void shutdown() {

        for (AFitlyDatasource<K> datasource : datasourceMap.values()) {
            datasource.closeDatasource();
        }

        datasourceMap.clear();
        routerMap.clear();
    }

    public void closeUnusedDatasources() {
        Set<String> expiredKeys = datasourceMap.entrySet().stream()
                .filter(e -> e.getValue().isExpired())
                .map(Map.Entry::getKey)
                .collect(Collectors.toSet());

        for (String key : expiredKeys) {
            AFitlyDatasource<K> ds = datasourceMap.remove(key);
            if (ds != null) {
                ds.closeDatasource();
            }
        }

        routerMap.values().removeIf(info -> expiredKeys.contains(info.getDatasourceKey()));
    }

}
