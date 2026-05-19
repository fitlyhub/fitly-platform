/**
 * Project: Fitly Platform
 * Author:  fitly.zero
 * Date:    May 11, 2026
 * Time:    1:39:48 PM
 * * Copyright (c) 2026 fitly.zero. All rights reserved.
 * Licensed under the Apache License 2.0.
 */
package vn.fitly.iam.dao.impl.pg;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

import vn.fitly.foundation.helper.DbHelper;
import vn.fitly.iam.dao.UserDao;
import vn.fitly.iam.model.User;

/**
 * 
 */
public class PgGlobalUserDaoImpl implements UserDao {

    @Override
    public User getUserByUsername(String username) throws Exception {

        String sql = "select * from sys_user where username = ?";

        try (PreparedStatement ps = DbHelper.preparedStatement(sql, username);
                ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {

                User user = new User();
                user.setUserId(rs.getString("sys_user_id"));
                user.setPassword(rs.getString("password"));
                user.setActive(rs.getBoolean("is_active"));

                return user;
            }
        }

        return null;
    }

}
