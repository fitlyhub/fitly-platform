/**
 * Project: Fitly Platform
 * Author:  fitly.zero
 * Date:    May 13, 2026
 * Time:    10:27:29 AM
 * * Copyright (c) 2026 fitly.zero. All rights reserved.
 * Licensed under the Apache License 2.0.
 */
package vn.fitly.foundation.cache;

import java.time.Duration;
import java.time.temporal.ChronoUnit;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;

import vn.fitly.foundation.utils.FoundationConstant;

/**
 * 
 */
public class LocalCache implements ICacheStore {

    public static final LocalCache LOCAL_CACHE = new LocalCache();

    protected static LocalCache getInstance() {
        return LOCAL_CACHE;
    }

    private LocalCache() {
    }

    private final Cache<String, CacheData> CACHE = Caffeine.newBuilder()
            .maximumSize(5000)
            .expireAfterWrite(Duration.of(FoundationConstant.CACHE_EXPIRED_SECONDS, ChronoUnit.SECONDS))
            .build();

    @Override
    public void put(String key, Object value, long ttl) {

        if (value == null) {
            return;
        }

        CACHE.put(key, new CacheData(value, ttl));
    }

    @Override
    public void remove(String key) {
        CACHE.invalidate(key);
    }

    @Override
    public void clearCache() {
        CACHE.invalidateAll();
    }

    @Override
    public <T> T get(String key, Class<T> clazz) {
        CacheData obj = CACHE.getIfPresent(key);
        if (obj == null) {
            return null;
        }

        if (obj.isExpire()) {
            CACHE.invalidate(key);
            return null;
        }

        return clazz.cast(obj.data);
    }

    @Override
    public boolean isEnable() {
        return true;
    }

    private class CacheData {

        private final Object data;

        private final long createdAt;

        private final long ttlSeconds;

        private CacheData(Object data, long ttlSeconds) {
            this.data = data;
            this.createdAt = System.currentTimeMillis();
            this.ttlSeconds = ttlSeconds;
        }

        boolean isExpire() {

            long delta = System.currentTimeMillis() - this.createdAt;
            return delta > this.ttlSeconds * 1000;
        }

    }

}
