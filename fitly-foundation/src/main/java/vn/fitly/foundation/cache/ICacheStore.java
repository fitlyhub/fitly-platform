/**
 * Project: Fitly Platform
 * Author:  fitly.zero
 * Date:    May 13, 2026
 * Time:    10:20:04 AM
 * * Copyright (c) 2026 fitly.zero. All rights reserved.
 * Licensed under the Apache License 2.0.
 */
package vn.fitly.foundation.cache;

/**
 * 
 */
public interface ICacheStore {

    public void put(String key, Object value, long ttl);

    public void remove(String key);

    public void clearCache();

    public <T> T get(String key, Class<T> clazz);

    public boolean isEnable();
}
