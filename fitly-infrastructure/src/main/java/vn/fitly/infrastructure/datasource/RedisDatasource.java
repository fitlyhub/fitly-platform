/**
 * Project: Fitly Platform
 * Author:  fitly.zero
 * Date:    Apr 30, 2026
 * Time:    10:50:23 AM
 * * Copyright (c) 2026 fitlyzero. All rights reserved.
 * Licensed under the Apache License 2.0.
 */
package vn.fitly.infrastructure.datasource;

import redis.clients.jedis.ConnectionPoolConfig;
import redis.clients.jedis.DefaultJedisClientConfig;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisClientConfig;
import redis.clients.jedis.RedisClient;
import vn.fitly.common.exception.FitlyRuntimeException;
import vn.fitly.infrastructure.dto.DatasourceInfo;

/**
 * 
 */
public class RedisDatasource extends ADatasource<RedisClient> {

    private RedisClient datasource;

    public RedisDatasource(DatasourceInfo datasourceInfo) {
        super(datasourceInfo);
    }

    @Override
    protected RedisClient getConnectionFromDatasource() throws FitlyRuntimeException {
        // RedisClient is thread-safe and manages its own pool internally
        return datasource;
    }

    @Override
    protected void initDatasource(DatasourceInfo datasourceInfo) {

        ConnectionPoolConfig poolConfig = new ConnectionPoolConfig();
        poolConfig.setMaxTotal(datasourceInfo.getMaxConnection());
        poolConfig.setMaxIdle(datasourceInfo.getMaxConnection());
        poolConfig.setMinIdle(datasourceInfo.getMinConnection());
        poolConfig.setTestOnBorrow(true);
        poolConfig.setTestOnReturn(true);
        poolConfig.setTestWhileIdle(true);
        poolConfig.setNumTestsPerEvictionRun(3);
        poolConfig.setBlockWhenExhausted(true);

        HostAndPort hostAndPort = new HostAndPort(
                datasourceInfo.getHost(),
                datasourceInfo.getPort());

        JedisClientConfig clientConfig = DefaultJedisClientConfig.builder()
                .password(datasourceInfo.getPassword())
                .database(datasourceInfo.getRedisDatabaseName())
                .socketTimeoutMillis(1000)
                .build();

        datasource = RedisClient.builder()
                .hostAndPort(hostAndPort)
                .clientConfig(clientConfig)
                .poolConfig(poolConfig)
                .build();
    }

    @Override
    public void closeDatasource() {
        if (datasource != null) {
            datasource.close();
        }
    }

}
