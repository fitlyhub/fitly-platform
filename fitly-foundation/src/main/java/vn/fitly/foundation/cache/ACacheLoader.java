/**
 * Project: Fitly Platform
 * Author:  fitly.zero
 * Date:    May 13, 2026
 * Time:    11:30:09 AM
 * * Copyright (c) 2026 fitly.zero. All rights reserved.
 * Licensed under the Apache License 2.0.
 */
package vn.fitly.foundation.cache;

import vn.fitly.common.exception.ErrorStatus;
import vn.fitly.common.exception.FitlyRuntimeException;
import vn.fitly.common.language.DefaultSystemMessage;
import vn.fitly.common.lock.FitlyLock;
import vn.fitly.common.utils.StringUtils;

/**
 * 
 */
public abstract class ACacheLoader<T> {

    protected abstract T query() throws Exception;

    protected abstract String getObjectKey();

    protected boolean isUseOnlyLocalCache() {
        return false;
    }

    protected final Class<T> clazz;

    protected final String prefix;

    public ACacheLoader(String prefix, Class<T> clazz) {
        this.clazz = clazz;
        this.prefix = prefix;
    }

    public T get() {

        String key = StringUtils.mergeKey(prefix, getObjectKey());

        T obj = getFromCache();
        if (obj != null) {
            return obj;
        }

        return FitlyLock.supplyWithLock(key, () -> {

            T val = getFromCache();
            if (val != null) {
                return val;
            }

            try {
                val = query();
            } catch (Exception e) {
                throw new FitlyRuntimeException(ErrorStatus.INTERNAL_ERROR,
                        DefaultSystemMessage.INTERNAL_ERROR.name(),
                        e);
            }
            putToCache(val);

            return val;
        });

    }

    private T getFromCache() {

        String key = StringUtils.mergeKey(prefix, getObjectKey());

        if (!isUseOnlyLocalCache() && RedisCache.getInstance().isEnable()) {
            T obj = RedisCache.getInstance().get(key, clazz);

            if (obj != null) {
                return obj;
            }
        }

        return LocalCache.getInstance().get(key, clazz);

    }

    private void putToCache(T obj) {
        String key = StringUtils.mergeKey(prefix, getObjectKey());

        if (!isUseOnlyLocalCache() && RedisCache.getInstance().isEnable()) {
            RedisCache.getInstance().put(key, obj);
            return;
        }

        LocalCache.getInstance().put(key, obj);
    }
}
