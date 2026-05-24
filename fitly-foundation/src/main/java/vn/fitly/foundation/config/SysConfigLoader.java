/**
 * Project: Fitly Platform
 * Author:  fitly.zero
 * Date:    May 13, 2026
 * Time:    1:20:17 PM
 * * Copyright (c) 2026 fitly.zero. All rights reserved.
 * Licensed under the Apache License 2.0.
 */
package vn.fitly.foundation.config;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import vn.fitly.foundation.cache.ADaoWithCache;
import vn.fitly.foundation.helper.DbHelper;

/**
 * 
 */
public class SysConfigLoader extends ADaoWithCache<String, String> {

    private static final SysConfigLoader loader = new SysConfigLoader();
    
    private SysConfigLoader() {
        super();
    }
    
    public static String getConfigValue(String name) {
        return loader.getById(name);
    }

    @Override
    protected String getPrefix() {
        return "sys_config";
    }

    @Override
    protected Class<String> getClazz() {
        return String.class;
    }

    @Override
    protected Map<String, String> loadDataWithIds(Collection<String> ids) throws Exception {
        String sql = "select value from sys_config where is_active = true and name = ?";

        try (PreparedStatement ps = DbHelper.preparedStatement(sql, ids);
                ResultSet rs = ps.executeQuery()) {

            Map<String, String> rsMap = new HashMap<>();
            while (rs.next()) {

            }

            return rsMap;
        }

    }

}
