/**
 * Project: Fitly Platform
 * Author:  fitly.zero
 * Date:    May 23, 2026
 * Time:    2:09:47 PM
 * * Copyright (c) 2026 fitly.zero. All rights reserved.
 * Licensed under the Apache License 2.0.
 */
package vn.fitly.foundation.cache;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import vn.fitly.common.exception.ErrorStatus;
import vn.fitly.common.exception.FitlyRuntimeException;
import vn.fitly.common.utils.StringUtils;

/**
 * 
 */
public abstract class ADaoWithCache<K, T> {

    private static final Map<String, String> DATA_PROCESSING_MAP = new ConcurrentHashMap<>();

    protected abstract String getPrefix();

    protected abstract Class<T> getClazz();

    protected abstract Map<K, T> loadDataWithIds(Collection<K> ids) throws Exception;

    protected long getTtlSeconds() {
        return 60 * 60;
    }

    public T getById(K id) {
        return getByIds(Arrays.asList(id)).get(id);
    }

    public Map<K, T> getByIds(Collection<K> ids) {
        if (ids == null || ids.isEmpty()) {
            return new HashMap<>();
        }

        Map<K, T> rsMap = new HashMap<>();

        Set<K> needQuerySet = new HashSet<>();

        Set<K> waitOtherThreadSet = new HashSet<>();

        for (K id : ids) {

            String key = buildKey(id);
            T obj = CacheManager.get(key, getClazz());
            if (obj != null) {
                rsMap.put(id, obj);
                continue;
            }

            String owner = DATA_PROCESSING_MAP.putIfAbsent(key, Thread.currentThread().getName());

            if (owner == null) {
                needQuerySet.add(id);
                continue;
            }

            waitOtherThreadSet.add(id);

        }

        if (!needQuerySet.isEmpty()) {
            rsMap.putAll(loadAndPutToCache(needQuerySet));
        }

        if (!waitOtherThreadSet.isEmpty()) {
            rsMap.putAll(waitUntilLoaded(waitOtherThreadSet));
        }

        return rsMap;
    }

    private Map<K, T> waitUntilLoaded(Set<K> waitSet) {

        Map<K, T> rsMap = new HashMap<>();

        for (int i = 0; i < 5; i++) {

            Set<K> remainingSet = new HashSet<>(waitSet);

            for (K id : remainingSet) {
                T obj = CacheManager.get(buildKey(id), getClazz());
                if (obj != null) {
                    rsMap.put(id, obj);
                    waitSet.remove(id);
                    continue;
                }
            }

            if (waitSet.isEmpty()) {
                return rsMap;
            }

            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                throw new FitlyRuntimeException(ErrorStatus.INTERNAL_ERROR, ErrorStatus.INTERNAL_ERROR.name());

            }

        }

        if (waitSet.isEmpty()) {
            return rsMap;
        }

        rsMap.putAll(loadAndPutToCache(waitSet));
        return rsMap;

    }

    private String buildKey(K id) {
        return StringUtils.mergeKey(getPrefix(), id.toString());
    }

    private Map<K, T> loadAndPutToCache(Collection<K> ids) {

        try {
            Map<K, T> loadedMap = loadDataWithIds(ids);
            if (loadedMap == null || loadedMap.isEmpty()) {
                return loadedMap;
            }

            for (Entry<K, T> entry : loadedMap.entrySet()) {
                CacheManager.put(
                        buildKey(entry.getKey()),
                        entry.getValue(),
                        getTtlSeconds());
            }

            return loadedMap;

        } catch (Exception e) {
            throw new FitlyRuntimeException(ErrorStatus.INTERNAL_ERROR, ErrorStatus.INTERNAL_ERROR.name());
        }

        finally {
            for (K id : ids) {
                DATA_PROCESSING_MAP.remove(buildKey(id));
            }
        }
    }
}
