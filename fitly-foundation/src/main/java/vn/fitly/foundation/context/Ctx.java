/**
 * Project: Fitly Platform
 * Author:  fitly.zero
 * Date:    May 10, 2026
 * Time:    4:41:44 PM
 * * Copyright (c) 2026 fitly.zero. All rights reserved.
 * Licensed under the Apache License 2.0.
 */
package vn.fitly.foundation.context;

import java.sql.Connection;

import vn.fitly.infrastructure.datasource.tenant.DBProvider;

/**
 * 
 */
public class Ctx implements AutoCloseable {

    private final boolean isReadOnly;

    private UserPrincipal user;

    private Connection connection;

    /**
     * @param tenantId
     * @param user
     * @param connection
     */
    public Ctx(boolean isReadOnly) {
        this.isReadOnly = isReadOnly;
    }

    /**
     * @return the user
     */
    public UserPrincipal getUser() {
        return user;
    }

    /**
     * @param user the user to set
     */
    public void setUser(UserPrincipal user) {
        this.user = user;
    }

    /**
     * @return the connection
     * @throws Exception
     */
    public Connection getConnection() throws Exception {

        if (connection == null) {

            connection = DBProvider.getConnection();
            connection.setReadOnly(isReadOnly);
            connection.setAutoCommit(false);
        }

        return connection;
    }

    @Override
    public void close() throws Exception {

        if (connection != null) {
            connection.close();
            connection = null;
        }

        user = null;

    }

    public void commit() throws Exception {
        if (connection == null) {
            return;
        }

        connection.commit();
    }

    public void rollback() throws Exception {
        if (connection == null) {
            return;
        }

        connection.rollback();
    }

}
