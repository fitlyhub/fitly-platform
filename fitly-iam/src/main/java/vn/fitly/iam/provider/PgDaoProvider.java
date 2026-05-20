/**
 * Project: Fitly Platform
 * Author:  fitly.zero
 * Date:    May 13, 2026
 * Time:    7:57:14 PM
 * * Copyright (c) 2026 fitly.zero. All rights reserved.
 * Licensed under the Apache License 2.0.
 */
package vn.fitly.iam.provider;

import java.util.Map;
import java.util.function.Supplier;

import com.google.auto.service.AutoService;

import vn.fitly.foundation.dao.DaoProvider;
import vn.fitly.iam.dao.RoleDao;
import vn.fitly.iam.dao.UserDao;
import vn.fitly.iam.dao.impl.pg.PgRoleDaoImpl;
import vn.fitly.iam.dao.impl.pg.PgUserDaoImpl;
import vn.fitly.infrastructure.datasource.FitlyDbType;

/**
 * 
 */
@AutoService(DaoProvider.class)
public class PgDaoProvider implements DaoProvider {

    @Override
    public FitlyDbType getDbType() {
        return FitlyDbType.POSTGRES;
    }

    @Override
    public void register(Map<String, Supplier<?>> registry) {
        registry.put(buildKey(UserDao.class), PgUserDaoImpl::new);
        registry.put(buildKey(RoleDao.class), PgRoleDaoImpl::new);

    }

}
