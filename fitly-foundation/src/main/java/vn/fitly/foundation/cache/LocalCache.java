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

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;

import vn.fitly.foundation.utils.FoundationConstant;

/**
 * 
 */
public class LocalCache implements ICache {

    public static final LocalCache LOCAL_CACHE = new LocalCache();

    protected static LocalCache getInstance() {
        return LOCAL_CACHE;
    }

    private LocalCache() {
    }

    private final Cache<String, Object> CACHE = Caffeine.newBuilder()
            .maximumSize(5000)
            .expireAfterWrite(Duration.ofMinutes(FoundationConstant.CACHE_EXPIRED_MINUTES))
            .build();

    @Override
    public void put(String key, Object value) {
        CACHE.put(key, value);
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
        Object obj = CACHE.getIfPresent(key);
        if (obj == null) {
            return null;
        }

        return clazz.cast(obj);
    }

    @Override
    public boolean isEnable() {
        return true;
    }

}
