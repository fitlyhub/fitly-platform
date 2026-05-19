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

import vn.fitly.common.exception.ErrorStatus;
import vn.fitly.common.exception.FitlyBussinessException;
import vn.fitly.common.language.DefaultSystemMessage;
import vn.fitly.foundation.cache.ACacheLoader;
import vn.fitly.foundation.helper.DbHelper;

/**
 * 
 */
public class SysConfigLoader<T> extends ACacheLoader<T> {

    private final String configKey;

    public SysConfigLoader(String configKey, Class<T> clazz) {
        super("sysconfig", clazz);
        this.configKey = configKey;

    }

    public T getWithDefault(T defaultVal) {
        T val = get();
        if (val == null) {
            return defaultVal;
        }

        return val;
    }

    @Override
    protected T query() throws Exception {

        String sql = "select value from sys_config where is_active = true and name = ?";

        try (PreparedStatement ps = DbHelper.preparedStatement(sql, configKey);
                ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                String val = rs.getString("value");
                return parseValue(val);
            }
        }

        return null;
    }

    @Override
    protected String getObjectKey() {
        return this.configKey;
    }

    @SuppressWarnings("unchecked")
    private T parseValue(String val) {

        if (val == null) {
            return null;
        }

        if (clazz == String.class) {
            return (T) val;
        } else if (clazz == Integer.class || clazz == int.class) {
            return (T) Integer.valueOf(val);
        } else if (clazz == Boolean.class || clazz == boolean.class) {

            if ("Y".equals(val)) {
                return (T) Boolean.TRUE;
            }

            if ("N".equals(val)) {
                return (T) Boolean.FALSE;
            }

            return (T) Boolean.valueOf(val);

        } else if (clazz == Long.class || clazz == long.class) {
            return (T) Long.valueOf(val);
        } else if (clazz == Double.class || clazz == double.class) {
            return (T) Double.valueOf(val);
        }

        // Nếu tương lai bạn lưu JSON trong config và muốn parse ra một Object cụ thể:
        // if (SystemConfigObject.class.isAssignableFrom(clazz)) {
        // return ObjectMapper.readValue(value, clazz);
        // }

        throw new FitlyBussinessException(ErrorStatus.INTERNAL_ERROR, DefaultSystemMessage.INTERNAL_ERROR.name());
    }
}
