/**
 * Project: Fitly Platform
 * Author:  fitly.zero
 * Date:    May 13, 2026
 * Time:    1:28:40 AM
 * * Copyright (c) 2026 fitly.zero. All rights reserved.
 * Licensed under the Apache License 2.0.
 */
package vn.fitly.infrastructure.datasource.tenant;

import java.sql.Connection;
import java.sql.SQLException;

import vn.fitly.common.exception.ErrorStatus;
import vn.fitly.common.exception.FitlyRuntimeException;
import vn.fitly.common.language.DefaultSystemMessage;
import vn.fitly.infrastructure.datasource.PostgresDatasource;
import vn.fitly.infrastructure.dto.DatasourceInfo;

/**
 * 
 */
public class PostgresMultiDatasource extends AMultiDatasource<Connection> {

    private static final PostgresMultiDatasource INSTANCE = new PostgresMultiDatasource();

    private PostgresMultiDatasource() {
    }

    protected static PostgresMultiDatasource getInstance() {
        return INSTANCE;
    }

    @Override
    protected PostgresDatasource createDatasource(DatasourceInfo datasourceInfo) {
        return new PostgresDatasource(datasourceInfo);
    }

    @Override
    protected void setSchema(Connection connection, String schema) throws FitlyRuntimeException {
        try {
            connection.setSchema(schema);
        } catch (SQLException e) {
            throw new FitlyRuntimeException(ErrorStatus.INTERNAL_ERROR, DefaultSystemMessage.INTERNAL_ERROR.name(), e);
        }
    }

}
