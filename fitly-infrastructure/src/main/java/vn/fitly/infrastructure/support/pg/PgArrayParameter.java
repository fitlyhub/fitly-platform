/**
 * Project: Fitly Platform Author:  fitly.zero Date:    May 24, 2026 Time:    2:54:00 PM * Copyright (c) 2026
 * fitly.zero. All rights reserved. Licensed under the Apache License 2.0.
 */
package vn.fitly.infrastructure.support.pg;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

/**
 *
 */
public class PgArrayParameter {

    private final PgObjectType type;

    private final List<Object> dataList = new ArrayList<>();
    ;

    private PgArrayParameter(PgObjectType type, Collection<? extends Object> dataList) {
        this.type = type;
        this.dataList.addAll(dataList);

    }

    public static PgArrayParameter setArrayInt(Collection<Integer> dataList) {
        return new PgArrayParameter(PgObjectType.INT4, dataList);
    }

    public static PgArrayParameter setArrayLong(Collection<Long> dataList) {
        return new PgArrayParameter(PgObjectType.INT8, dataList);
    }

    public static PgArrayParameter setArrayString(Collection<String> dataList) {
        return new PgArrayParameter(PgObjectType.VARCHAR, dataList);
    }

    public static PgArrayParameter setArrayUUID(Collection<UUID> dataList) {
        return new PgArrayParameter(PgObjectType.UUID, dataList);
    }

}
