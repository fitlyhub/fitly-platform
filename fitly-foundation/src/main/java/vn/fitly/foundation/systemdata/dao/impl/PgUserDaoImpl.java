/**
 * Project: Fitly Platform
 * Author:  fitly.zero
 * Date:    May 11, 2026
 * Time:    1:39:48 PM
 * * Copyright (c) 2026 fitly.zero. All rights reserved.
 * Licensed under the Apache License 2.0.
 */
package vn.fitly.foundation.systemdata.dao.impl;

import java.util.UUID;

import vn.fitly.foundation.context.Ctx;
import vn.fitly.foundation.systemdata.dao.UserDao;
import vn.fitly.foundation.systemdata.entity.User;
import vn.fitly.infrastructure.query.QueryExecutor;
import vn.fitly.infrastructure.query.QueryInput;

/**
 * 
 */
public class PgUserDaoImpl implements UserDao {

    @Override
    public User getUserByUsername(String username) throws Exception {

        QueryInput query = QueryInput.getQueryInput();
        query.appendSql("select *\n");
        query.appendSql("from sys_user\n");
        query.appendSql("where ");
        query.addWhereClause("username", username);

        return QueryExecutor.query(Ctx.getConnection(), query, rs -> {

            if (rs.next()) {
                User user = new User();
                user.setUserId(rs.getObject("sys_user_id", UUID.class));
                user.setUsername(rs.getString("username"));
                user.setPassword(rs.getString("password"));
                user.setActive(rs.getBoolean("is_active"));

                return user;
            }

            return null;
        });

    }

}
