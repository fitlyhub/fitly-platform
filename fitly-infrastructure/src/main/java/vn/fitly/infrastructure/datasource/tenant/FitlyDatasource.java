/**
 * Project: Fitly Platform
 * Author:  fitly.zero
 * Date:    Apr 30, 2026
 * Time:    11:11:22 AM
 * * Copyright (c) 2026 fitlyzero. All rights reserved.
 * Licensed under the Apache License 2.0.
 */
package vn.fitly.infrastructure.datasource.tenant;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import vn.fitly.common.exception.ErrorStatus;
import vn.fitly.common.exception.FitlyRuntimeException;
import vn.fitly.common.language.DefaultSystemMessage;
import vn.fitly.infrastructure.config.ApplicationConfig;
import vn.fitly.infrastructure.datasource.PostgresDatasource;
import vn.fitly.infrastructure.dto.DatasourceInfo;

/**
 * 
 */
public class FitlyDatasource {

    private static final PostgresDatasource datasource = new PostgresDatasource(ApplicationConfig.getDatasource());

    public static Connection getConnection() {
        return datasource.getConnection();
    }

    public static DatasourceInfo loadDatasource(String serviceName) throws FitlyRuntimeException {

        String sql = "select * from sys_tenant_datasource where service_name = ?";

        try (Connection conn = FitlyDatasource.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, serviceName);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    DatasourceInfo connectionInfo = new DatasourceInfo();
                    connectionInfo.initTenantDatasource(rs);
                    return connectionInfo;
                }
            }
            return null;
        } catch (Exception e) {
            throw new FitlyRuntimeException(ErrorStatus.INTERNAL_ERROR, DefaultSystemMessage.INTERNAL_ERROR.name(), e);
        }
    }

}
