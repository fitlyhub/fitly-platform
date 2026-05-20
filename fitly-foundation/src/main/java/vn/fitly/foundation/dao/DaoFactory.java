/**
 * Project: Fitly Platform
 * Author:  fitly.zero
 * Date:    May 11, 2026
 * Time:    2:07:13 PM
 * * Copyright (c) 2026 fitly.zero. All rights reserved.
 * Licensed under the Apache License 2.0.
 */
package vn.fitly.foundation.dao;

import java.util.HashMap;
import java.util.Map;
import java.util.ServiceLoader;
import java.util.function.Supplier;

import vn.fitly.common.utils.StringUtils;
import vn.fitly.infrastructure.config.ApplicationConfig;

/**
 * 
 */
public class DaoFactory {

    private static final Map<String, Supplier<?>> REGISTRY = new HashMap<>();

    private DaoFactory() {
    }

    static {
        ServiceLoader<DaoProvider> loader = ServiceLoader.load(DaoProvider.class);
        for (DaoProvider provider : loader) {
            provider.register(REGISTRY);
        }
    }

    @SuppressWarnings("unchecked")
    public static <T> T getDao(Class<T> daoType) {

        String key = StringUtils.merge(ApplicationConfig.getDatabaseType().name().toLowerCase(), ":", daoType.getName());

        Supplier<?> supplier = REGISTRY.get(key);

        if (supplier == null) {
            throw new RuntimeException("No DAO implementation found for key: " + key);
        }

        return (T) supplier.get();
    }

}
