/**
 * Project: Fitly Flatform
 *
 * Author:  fitly.zero
 *
 * Copyright (c) 2026 fitly.zero. All rights reserved.
 *
 * Licensed under the Apache License 2.0.
 */
package vn.fitly.infrastructure.dto;

import java.sql.ResultSet;

import vn.fitly.common.utils.StringUtils;
import vn.fitly.infrastructure.datasource.FitlyDbType;
import vn.fitly.infrastructure.spring.initializer.FitlyDatasourceConfig;

public class DatasourceInfo {

    private String tenantId;

    private String serviceName;

    private String host;
    // default port
    private int port = 5432;

    private String databaseName;

    private String schema;

    private String username;

    private String password;
    // default connection
    private int maxConnection = 10;

    private int minConnection = 2;

    private FitlyDbType type;

    public void initMasterDatasource(FitlyDatasourceConfig config) {
        this.tenantId = "Fitly-Master";
        this.serviceName = "Fitly-Master";
        this.host = config.host();
        this.port = config.port();
        this.databaseName = config.databaseName();
        this.schema = config.schema();
        this.username = config.username();
        this.password = config.password();
        this.maxConnection = 5;
        this.minConnection = 1;
        this.type = FitlyDbType.getType(config.type());
    }

    public void initTenantDatasource(ResultSet rs) throws Exception {

        this.tenantId = rs.getString("tenant_id");
        this.serviceName = rs.getString("service_name");
        this.host = rs.getString("host");
        this.port = rs.getInt("port");
        this.databaseName = rs.getString("database_name");
        this.schema = rs.getString("schema");
        this.username = rs.getString("username");
        this.password = rs.getString("password");
        this.maxConnection = rs.getInt("max_connection");
        this.minConnection = rs.getInt("min_connection");
        this.type = FitlyDbType.getType(rs.getString("db_type"));
    }

    /**
     * @return the tenantId
     */
    public String getTenantId() {
        return tenantId;
    }

    /**
     * @return the serviceName
     */
    public String getServiceName() {
        return serviceName;
    }

    /**
     * @return the host
     */
    public String getHost() {
        return host;
    }

    /**
     * @return the port
     */
    public int getPort() {
        return port;
    }

    /**
     * @return the databaseName
     */
    public String getDatabaseName() {
        return databaseName;
    }

    public int getRedisDatabaseName() {
        try {
            return Integer.parseInt(databaseName);
        } catch (Exception e) {
            return 0;
        }
    }

    /**
     * @return the schema
     */
    public String getSchema() {
        return schema;
    }

    /**
     * @return the username
     */
    public String getUsername() {
        return username;
    }

    /**
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * @return the maxConnection
     */
    public int getMaxConnection() {
        return maxConnection;
    }

    /**
     * @return the minConnection
     */
    public int getMinConnection() {
        return minConnection;
    }

    /**
     * @return the type
     */
    public FitlyDbType getType() {
        return type;
    }

    public String getUrl() {
        String url = "jdbc:postgresql://" + host + ":" + port + "/" + databaseName;
        return url;
    }

    public String getDatasourceKey() {
        return StringUtils.mergeKey(host, String.valueOf(port), databaseName);
    }

}
