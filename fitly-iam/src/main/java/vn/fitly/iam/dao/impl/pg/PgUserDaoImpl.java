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
import java.util.UUID;

import vn.fitly.foundation.helper.DbHelper;
import vn.fitly.iam.dao.UserDao;
import vn.fitly.iam.model.User;

/**
 * 
 */
public class PgUserDaoImpl implements UserDao {

    @Override
    public User getUserByUsername(String username) throws Exception {

        String sql = "select * from sys_user where username = ?";

        try (PreparedStatement ps = DbHelper.preparedStatement(sql, username);
                ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                return mapUser(rs);
            }
        }

        return null;
    }

    @Override
    public User getUserById(UUID userId) throws Exception {

        String sql = "select * from sys_user where sys_user_id = ?";

        try (PreparedStatement ps = DbHelper.preparedStatement(sql, userId);
                ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                return mapUser(rs);
            }
        }

        return null;
    }

    private User mapUser(ResultSet rs) throws Exception {

        User user = new User();
        user.setUserId(UUID.fromString(rs.getString("sys_user_id")));
        user.setUsername(rs.getString("username"));
        user.setPassword(rs.getString("password"));
        user.setAvatarUrl(rs.getString("avatar_url"));
        user.setFirstName(rs.getString("first_name"));
        user.setLastName(rs.getString("last_name"));
        user.setFullName(rs.getString("full_name"));
        user.setActive(rs.getBoolean("is_active"));

        return user;
    }

}
