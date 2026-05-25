/**
 * Project: Fitly Platform
 * Author:  fitly.zero
 * Date:    May 24, 2026
 * Time:    2:44:47 PM
 * * Copyright (c) 2026 fitly.zero. All rights reserved.
 * Licensed under the Apache License 2.0.
 */
package vn.fitly.infrastructure.query;

import java.sql.ResultSet;

/**
 * 
 */
public interface ResultSetHandler<T> {
    T handle(ResultSet rs) throws Exception;
}
