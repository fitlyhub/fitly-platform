/**
 * Project: Fitly Platform
 * Author:  fitly.zero
 * Date:    May 22, 2026
 * Time:    2:55:10 PM
 * * Copyright (c) 2026 fitly.zero. All rights reserved.
 * Licensed under the Apache License 2.0.
 */
package vn.fitly.foundation.cache;

/**
 * 
 */
public class CacheManager {

    protected static void put(String key, Object value, long ttl) {

        if (RedisCache.getInstance().isEnable()) {
            RedisCache.getInstance().put(key, value, ttl);
            return;
        }

        LocalCache.getInstance().put(key, value, ttl);
    }

    protected static <T> T get(String key, Class<T> clazz) {
        if (RedisCache.getInstance().isEnable()) {
            T obj = RedisCache.getInstance().get(key, clazz);
            if (obj != null) {
                return obj;
            }
        }

        return LocalCache.getInstance().get(key, clazz);
    }


    protected static void remove(String key) {

        if (RedisCache.getInstance().isEnable()) {
            RedisCache.getInstance().remove(key);
        }

        LocalCache.getInstance().remove(key);
    }
}
