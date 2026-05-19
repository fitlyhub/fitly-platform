/**
 * Project: Fitly Platform
 * Author:  fitly.zero
 * Date:    May 13, 2026
 * Time:    10:27:29 AM
 * * Copyright (c) 2026 fitly.zero. All rights reserved.
 * Licensed under the Apache License 2.0.
 */
package vn.fitly.foundation.cache;

import redis.clients.jedis.RedisClient;
import redis.clients.jedis.params.SetParams;
import tools.jackson.databind.ObjectMapper;
import vn.fitly.common.json.JsonMapperBuilder;
import vn.fitly.foundation.utils.FoundationConstant;
import vn.fitly.infrastructure.datasource.tenant.DBProvider;

/**
 * 
 */
public class RedisCache implements ICache {

    public static final RedisCache REDIS_CACHE = new RedisCache();

    protected static RedisCache getInstance() {
        return REDIS_CACHE;
    }

    private static final String SERVICE_NAME = "cache";

    private static final ObjectMapper REDIS_MAPPER = JsonMapperBuilder.build();

    private RedisCache() {
    }

    @Override
    public void put(String key, Object value) {
        RedisClient redisClient = DBProvider.getRedisClient(SERVICE_NAME);
        redisClient.set(key,
                REDIS_MAPPER.writeValueAsString(value),
                SetParams.setParams().ex(FoundationConstant.CACHE_EXPIRED_MINUTES * 60));
    }

    @Override
    public void remove(String key) {
        RedisClient redisClient = DBProvider.getRedisClient(SERVICE_NAME);
        redisClient.del(key);
    }

    @Override
    public void clearCache() {
        RedisClient redisClient = DBProvider.getRedisClient(SERVICE_NAME);
        redisClient.flushDB();
    }

    @Override
    public <T> T get(String key, Class<T> clazz) {
        RedisClient redisClient = DBProvider.getRedisClient(SERVICE_NAME);

        String val = redisClient.get(key);
        if (val == null || "nill".equals(val) || "null".equals(val)) {
            return null;
        }

        return REDIS_MAPPER.readValue(val, clazz);
    }

    @Override
    public boolean isEnable() {
        RedisClient redisClient = DBProvider.getRedisClient(SERVICE_NAME);
        return redisClient != null;
    }

}
