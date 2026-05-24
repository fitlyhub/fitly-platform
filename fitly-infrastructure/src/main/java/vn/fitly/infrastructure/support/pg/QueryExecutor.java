/**
 * Project: Fitly Platform
 * Author:  fitly.zero
 * Date:    May 24, 2026
 * Time:    2:45:32 PM
 * * Copyright (c) 2026 fitly.zero. All rights reserved.
 * Licensed under the Apache License 2.0.
 */
package vn.fitly.infrastructure.support.pg;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.sql.Types;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * 
 */
public class QueryExecutor {

    public static <T> T query(
            Connection conn,
            String sql,
            List<Object> params,
            ResultSetHandler<T> handler) throws Exception {

        try (PreparedStatement ps = conn.prepareStatement(sql)) {

            DbHelper.setParameters(conn, ps, holder, params);

            try (ResultSet rs = ps.executeQuery()) {
                return handler.handle(rs);
            }
        }
    }
    
    public static void setParameters(PreparedStatement ps, Object... paras) throws Exception {

        if (paras == null || paras.length == 0) {
            return;
        }

        for (int i = 0; i < paras.length; i++) {
            
            int index = i + 1;
            Object param = paras[i];

            if (param == null) {
                ps.setNull(index, Types.VARCHAR);
                continue;
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
                ps.setTimestamp(index, Timestamp.valueOf((LocalDateTime) param));
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

            throw new Exception(
                    "Unrecognized SQL parameter type at index " + index + ": " + param.getClass().getName());
        }
    }


}
