/**
 * Project: Fitly Platform
 * Author:  fitly.zero
 * Date:    May 8, 2026
 * Time:    11:48:18 AM
 * * Copyright (c) 2026 fitly.zero. All rights reserved.
 * Licensed under the Apache License 2.0.
 */
package vn.fitly.infrastructure.datasource.tenant;

import redis.clients.jedis.RedisClient;
import vn.fitly.infrastructure.datasource.RedisDatasource;
import vn.fitly.infrastructure.dto.DatasourceInfo;

/**
 * 
 */
public class RedisMultiDatasource extends AMultiDatasource<RedisClient> {

    private static final RedisMultiDatasource INSTANCE = new RedisMultiDatasource();

    private RedisMultiDatasource() {
    }

    protected static RedisMultiDatasource getInstance() {
        return INSTANCE;
    }

    @Override
    protected RedisDatasource createDatasource(DatasourceInfo datasourceInfo) {
        return new RedisDatasource(datasourceInfo);
    }

    @Override
    protected void setSchema(RedisClient connection, String schema) {
    }

}
