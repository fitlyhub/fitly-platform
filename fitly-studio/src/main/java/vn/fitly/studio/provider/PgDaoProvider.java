/**
 * Project: Fitly Platform
 * Author:  fitly.zero
 * Date:    May 21, 2026
 * Time:    2:00:00 PM
 * Copyright (c) 2026 fitly.zero. All rights reserved.
 * Licensed under the Apache License 2.0.
 */
package vn.fitly.studio.provider;

import java.util.Map;
import java.util.function.Supplier;

import com.google.auto.service.AutoService;

import vn.fitly.foundation.dao.DaoProvider;
import vn.fitly.infrastructure.datasource.FitlyDbType;
import vn.fitly.studio.dao.MenuDao;
import vn.fitly.studio.dao.impl.pg.PgMenuDaoImpl;

/**
 * Studio DAO provider for Postgres.
 */
@AutoService(DaoProvider.class)
public class PgDaoProvider implements DaoProvider {

    @Override
    public FitlyDbType getDbType() {
        return FitlyDbType.POSTGRES;
    }

    @Override
    public void register(Map<String, Supplier<?>> registry) {
        registry.put(buildKey(MenuDao.class), PgMenuDaoImpl::new);
    }
}
