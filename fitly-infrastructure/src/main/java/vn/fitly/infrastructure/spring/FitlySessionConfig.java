/**
 * Project: Fitly Platform
 * Author:  fitly.zero
 * Date:    May 21, 2026
 * Time:    2:00:00 PM
 * Copyright (c) 2026 fitly.zero. All rights reserved.
 * Licensed under the Apache License 2.0.
 */
package vn.fitly.infrastructure.spring;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Session configuration loaded from application.yml.
 *
 * Fitly uses server-side auth session with HttpOnly session cookie.
 */
@ConfigurationProperties(prefix = "fitly.session")
public class FitlySessionConfig {

    /**
     * Absolute session lifetime.
     *
     * Example: User logs in at 08:00. absoluteTimeoutMinutes = 600. Session must
     * expire at 18:00 even if user is still active.
     */
    private long absoluteTimeoutMinutes = 60 * 10;

    /**
     * Idle timeout.
     *
     * If user has no activity for this duration, session expires. Example: 5
     * minutes is common for ERP.
     */
    private long idleTimeoutMinutes = 5;

    /**
     * Maximum active sessions per user per tenant.
     *
     * Example: Chrome MacBook = 1 session Safari iPhone = 1 session Office PC = 1
     * session
     */
    private int maxActiveSession = 5;
    /**
     * Cookie name used to store session secret.
     */
    private String cookieName = "FITLY_SESSION";

    public long getAbsoluteTimeoutMinutes() {
        return absoluteTimeoutMinutes;
    }

    public void setAbsoluteTimeoutMinutes(long absoluteTimeoutMinutes) {
        this.absoluteTimeoutMinutes = absoluteTimeoutMinutes;
    }

    public long getIdleTimeoutMinutes() {
        return idleTimeoutMinutes;
    }

    public void setIdleTimeoutMinutes(long idleTimeoutMinutes) {
        this.idleTimeoutMinutes = idleTimeoutMinutes;
    }

    public int getMaxActiveSession() {
        return maxActiveSession;
    }

    public void setMaxActiveSession(int maxActiveSession) {
        this.maxActiveSession = maxActiveSession;
    }

    public String getCookieName() {
        return cookieName;
    }

    public void setCookieName(String cookieName) {
        this.cookieName = cookieName;
    }

}