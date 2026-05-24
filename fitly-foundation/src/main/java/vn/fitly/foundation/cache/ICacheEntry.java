/**
 * Project: Fitly Platform
 * Author:  fitly.zero
 * Date:    May 22, 2026
 * Time:    9:01:54 PM
 * * Copyright (c) 2026 fitly.zero. All rights reserved.
 * Licensed under the Apache License 2.0.
 */
package vn.fitly.foundation.cache;

import vn.fitly.foundation.utils.FoundationConstant;

/**
 * 
 */
public interface ICacheEntry<T> {

    public String getKey();

    public long getTtlSeconds();

    public Class<T> getClazz();

    default T get() {
        return CacheManager.get(getKey(), getClazz());
    }

    default void put(Object obj) {

        long ttl = getTtlSeconds();
        if (ttl < 1) {
            ttl = FoundationConstant.CACHE_EXPIRED_SECONDS;
        }
        CacheManager.put(getKey(), obj, getTtlSeconds());
    }

    default void remove() {
        CacheManager.remove(getKey());
    }
}
