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
import java.util.UUID;

import vn.fitly.foundation.systemdata.entity.Tenant;
import vn.fitly.infrastructure.datasource.tenant.DBProvider;

/**
 * 
 */
public class SessionContext implements AutoCloseable {

    private UserPrincipal user;

    private Tenant tenant;

    private Connection connection;

    private UUID sessionId;

    private String language;

    public SessionContext(UserPrincipal user, Tenant tenant, UUID sessionId, String language) {
        this.user = user;
        this.tenant = tenant;
        this.sessionId = sessionId;
        this.language = language;
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
     * @return the tenant
     */
    public Tenant getTenant() {
        return tenant;
    }

    public void setTenant(Tenant tenant) {
        this.tenant = tenant;
    }

    /**
     * @return the connection
     * @throws Exception
     */
    public Connection getConnection() throws Exception {

        if (connection == null) {

            connection = DBProvider.getConnection();
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
        tenant = null;
        sessionId = null;
        language = null;

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

    public UUID getSessionId() {
        return sessionId;
    }

    public void setSessionId(UUID sessionId) {
        this.sessionId = sessionId;
    }

    public UUID getTenantId() {
        return tenant == null ? null : tenant.getTenantId();
    }

    public UUID getUserId() {
        return user == null ? null : user.getUserId();
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

}
