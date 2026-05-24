/**
 * Project: Fitly Platform
 * Author:  fitly.zero
 * Date:    May 24, 2026
 * Time:    2:15:02 PM
 * * Copyright (c) 2026 fitly.zero. All rights reserved.
 * Licensed under the Apache License 2.0.
 */
package vn.fitly.infrastructure.support.pg;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collection;
import java.util.UUID;

/**
 * 
 */
public class PgPrepareStatement {

    private static void setParameters(Connection conn, PreparedStatement ps, Object... paras) throws Exception {

        if (paras == null || paras.length == 0) {
            return;
        }

        for (int i = 0; i < paras.length; i++) {

            int index = i + 1;
            Object param = paras[i];

            if (param == null) {
                ps.setObject(index, null);
                continue;
            }

            if (param instanceof Arrays || param instanceof Collection) {

            }

            if (param instanceof UUID) {
                ps.setObject(index, param);
                continue;
            }

            if (param instanceof String) {
                ps.setString(index, (String) param);
                continue;
            }

            if (param instanceof Long) {
                ps.setLong(index, (Long) param);
                continue;
            }

            if (param instanceof Integer) {
                ps.setInt(index, (Integer) param);
                continue;
            }

            if (param instanceof BigDecimal) {
                ps.setBigDecimal(index, (BigDecimal) param);
                continue;
            }

            if (param instanceof Boolean) {
                ps.setBoolean(index, (Boolean) param);
                continue;
            }

            if (param instanceof LocalDateTime) {
                ps.setObject(index, param);
                continue;
            }

            if (param instanceof Timestamp) {
                ps.setTimestamp(index, (Timestamp) param);
                continue;
            }

            if (param instanceof Double) {
                ps.setDouble(index, (Double) param);
                continue;
            }

            if (param instanceof byte[]) {
                ps.setBytes(index, (byte[]) param);
                continue;
            }

            ps.setObject(index, param);
        }
    }

//    public static PreparedStatement preparedStatement(String sql, Object... params) throws Exception {
//        PreparedStatement ps = Ctx.getConnection().prepareStatement(sql);
//        setParameters(ps, params);
//        return ps;
//    }
}
