/**
 * Project: Fitly Platform
 * Author:  fitly.zero
 * Date:    May 24, 2026
 * Time:    2:54:00 PM
 * * Copyright (c) 2026 fitly.zero. All rights reserved.
 * Licensed under the Apache License 2.0.
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

    private final List<Object> dataList = new ArrayList<>();;

    private PgArrayParameter(PgObjectType type, Collection<Object> dataList) {
        this.type = type;
        this.dataList.addAll(dataList);

    }
    
    public static setArrayInt(Collection)

    public PgArrayParameter(Collection<Integer> dataList) {
        this.type = PgObjectType.INT4;
        this.objectList.addAll(objectList);
    }

    public PgArrayParameter(Collection<Long> dataList) {
        this.type = PgObjectType.INT8;
        this.objectList.addAll(objectList);
    }

    public PgArrayParameter(Collection<UUID> dataList) {
        this.type = PgObjectType.UUID;
        this.objectList.addAll(objectList);
    }

}
