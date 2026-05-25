/**
 * Project: Fitly Platform Author:  fitly.zero Date:    May 24, 2026 Time:    2:54:00 PM * Copyright (c) 2026
 * fitly.zero. All rights reserved. Licensed under the Apache License 2.0.
 */
package vn.fitly.infrastructure.query.pg;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

import vn.fitly.infrastructure.query.DbObjectType;

/**
 *
 */
public class PgArrayParameter {

    private final DbObjectType type;

    private final List<Object> dataList = new ArrayList<>();
    

    public String getType() {
        return type.name().toLowerCase();
    }

    public List<Object> getDataList() {
        return dataList;
    }

    public PgArrayParameter(DbObjectType type, Collection<? extends Object> dataList) {
        this.type = type;
        this.dataList.addAll(dataList);

    }

    public static PgArrayParameter setArrayInt(Collection<Integer> dataList) {
        return new PgArrayParameter(DbObjectType.INT4, dataList);
    }

    public static PgArrayParameter setArrayLong(Collection<Long> dataList) {
        return new PgArrayParameter(DbObjectType.INT8, dataList);
    }

    public static PgArrayParameter setArrayString(Collection<String> dataList) {
        return new PgArrayParameter(DbObjectType.VARCHAR, dataList);
    }

    public static PgArrayParameter setArrayUUID(Collection<UUID> dataList) {
        return new PgArrayParameter(DbObjectType.UUID, dataList);
    }

}
