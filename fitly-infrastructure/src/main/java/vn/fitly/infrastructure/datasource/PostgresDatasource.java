/**
 * Project: Fitly Platform
 * Author:  fitly.zero
 * Date:    May 7, 2026
 * Time:    11:33:16 AM
 * * Copyright (c) 2026 fitly.zero. All rights reserved.
 * Licensed under the Apache License 2.0.
 */
package vn.fitly.infrastructure.datasource;

import java.sql.Connection;
import java.sql.SQLException;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import vn.fitly.common.exception.ErrorStatus;
import vn.fitly.common.exception.FitlyRuntimeException;
import vn.fitly.common.language.DefaultSystemMessage;
import vn.fitly.infrastructure.dto.DatasourceInfo;

/**
 * 
 */
public class PostgresDatasource extends AFitlyDatasource<Connection> {

    private HikariDataSource datasource;

    public PostgresDatasource(DatasourceInfo datasourceInfo) {
        super(datasourceInfo);
    }

    @Override
    protected Connection getConnectionFromDatasource() throws FitlyRuntimeException {
        try {
            return datasource.getConnection();
        } catch (SQLException e) {
            throw new FitlyRuntimeException(ErrorStatus.INTERNAL_ERROR, DefaultSystemMessage.INTERNAL_ERROR.name(), e);
        }
    }

    @Override
    protected void initDatasource(DatasourceInfo datasourceInfo) {
        HikariConfig config = new HikariConfig();

        config.setJdbcUrl(datasourceInfo.getUrl());
        config.setUsername(datasourceInfo.getUsername());
        config.setPassword(datasourceInfo.getPassword());
        config.setMaximumPoolSize(datasourceInfo.getMaxConnection());
        config.setMinimumIdle(datasourceInfo.getMinConnection());
        // 1 min
        config.setConnectionTimeout(1000 * 60);
        config.setIdleTimeout(1000 * 60 * 5);
        config.setMaxLifetime(1000 * 60 * 30);
        config.setDriverClassName("org.postgresql.Driver");

        datasource = new HikariDataSource(config);
    }

    @Override
    public void closeDatasource() {
        if (datasource != null) {
            datasource.close();
        }
    }

}
