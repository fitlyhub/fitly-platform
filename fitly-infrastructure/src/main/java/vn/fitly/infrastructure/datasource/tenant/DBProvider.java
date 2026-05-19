/**
 * Project: Fitly Platform
 * Author:  fitly.zero
 * Date:    May 13, 2026
 * Time:    12:27:17 AM
 * * Copyright (c) 2026 fitly.zero. All rights reserved.
 * Licensed under the Apache License 2.0.
 */
package vn.fitly.infrastructure.datasource.tenant;

import java.sql.Connection;

import redis.clients.jedis.RedisClient;

/**
 * 
 */
public class DBProvider {

    public static Connection getConnection() {
        return FitlyDatasource.getConnection();
    }

    public static RedisClient getRedisClient(String serviceName) {
        return RedisMultiDatasource.getInstance().getConnectionWithServiceName(serviceName);

    }

    public static void shutdown() {
        PostgresMultiDatasource.getInstance().shutdown();
        RedisMultiDatasource.getInstance().shutdown();

    }

    public static void closeUnusedDatasource() {

        PostgresMultiDatasource.getInstance().closeUnusedDatasources();
        RedisMultiDatasource.getInstance().closeUnusedDatasources();

    }

}
