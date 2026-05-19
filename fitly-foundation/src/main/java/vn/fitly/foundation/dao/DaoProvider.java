/**
 * Project: Fitly Platform
 * Author:  fitly.zero
 * Date:    May 11, 2026
 * Time:    2:02:20 PM
 * * Copyright (c) 2026 fitly.zero. All rights reserved.
 * Licensed under the Apache License 2.0.
 */
package vn.fitly.foundation.dao;

import java.util.Map;
import java.util.function.Supplier;

import vn.fitly.common.utils.StringUtils;
import vn.fitly.infrastructure.datasource.FitlyDbType;

/**
 * 
 */
public interface DaoProvider {

    FitlyDbType getDbType();

    void register(Map<String, Supplier<?>> registry);

    default String buildKey(Class<?> daoType) {
        return StringUtils.merge(getDbType().name().toLowerCase(), ":", daoType.getName());
    }
}
