/**
 * Project: Fitly Platform Author:  fitly.zero Date:    May 24, 2026 Time:    2:45:32 PM * Copyright (c) 2026
 * fitly.zero. All rights reserved. Licensed under the Apache License 2.0.
 */
package vn.fitly.infrastructure.query;

import vn.fitly.common.exception.ErrorCode;
import vn.fitly.common.exception.ErrorStatus;
import vn.fitly.common.exception.FitlyRuntimeException;
import vn.fitly.infrastructure.query.pg.PgArrayParameter;

import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 *
 */
public class QueryExecutor {

    public static <T> T query(
            Connection conn,
            QueryInput query,
            ResultSetHandler<T> handler) throws Exception {

        List<Array> arrayToFreeList = new ArrayList<>();

        try (PreparedStatement ps = conn.prepareStatement(query.getSql())) {

            int index = 1;
            for (Object param : query.getParams()) {

                if (param == null) {
                    ps.setObject(index++, null);
                    continue;
                }

                if (param instanceof PgArrayParameter) {

                    PgArrayParameter arrayParameter = (PgArrayParameter) param;
                    Array array = conn.createArrayOf(arrayParameter.getType(), arrayParameter.getDataList().toArray());
                    arrayToFreeList.add(array);

                    ps.setArray(index++, array);
                    continue;
                }

                setParameter(ps, param, index++);

            }

            try (ResultSet rs = ps.executeQuery()) {
                return handler.handle(rs);
            }

        } finally {
            for (Array array : arrayToFreeList) {
                array.free();
            }
        }
    }

    private static void setParameter(PreparedStatement ps, Object param, int index) throws Exception {

        if (param instanceof UUID) {
            ps.setObject(index, param);
            return;
        }

        if (param instanceof String) {
            ps.setString(index, (String) param);
            return;
        }

        if (param instanceof Long) {
            ps.setLong(index, (Long) param);
            return;
        }

        if (param instanceof Integer) {
            ps.setInt(index, (Integer) param);
            return;
        }

        if (param instanceof BigDecimal) {
            ps.setBigDecimal(index, (BigDecimal) param);
            return;
        }

        if (param instanceof Boolean) {
            ps.setBoolean(index, (Boolean) param);
            return;
        }

        if (param instanceof LocalDateTime) {
            ps.setTimestamp(index, Timestamp.valueOf((LocalDateTime) param));
            return;
        }

        if (param instanceof Timestamp) {
            ps.setTimestamp(index, (Timestamp) param);
            return;
        }

        if (param instanceof Double) {
            ps.setDouble(index, (Double) param);
            return;
        }

        if (param instanceof byte[]) {
            ps.setBytes(index, (byte[]) param);
            return;
        }

        throw new FitlyRuntimeException(ErrorStatus.INTERNAL_ERROR, ErrorCode.ERROR_WHILE_PROCESSING,
                "Unsupported SQL parameter type: " + param.getClass().getName());
    }
}