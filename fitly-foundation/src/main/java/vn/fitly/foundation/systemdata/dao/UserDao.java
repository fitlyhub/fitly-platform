/**
 * Project: Fitly Platform
 * Author:  fitly.zero
 * Date:    May 11, 2026
 * Time:    1:38:25 PM
 * * Copyright (c) 2026 fitly.zero. All rights reserved.
 * Licensed under the Apache License 2.0.
 */
package vn.fitly.foundation.systemdata.dao;

import vn.fitly.foundation.systemdata.entity.User;

/**
 * 
 */
public interface UserDao {

    public User getUserByUsername(String username) throws Exception;

}
