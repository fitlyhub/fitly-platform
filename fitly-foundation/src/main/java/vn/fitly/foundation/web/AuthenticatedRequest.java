package vn.fitly.foundation.web;

import vn.fitly.foundation.security.AuthenticatedSession;

public final class AuthenticatedRequest {

    private static final ThreadLocal<AuthenticatedSession> CURRENT = new ThreadLocal<>();

    private AuthenticatedRequest() {
    }

    public static AuthenticatedSession get() {
        return CURRENT.get();
    }

    static void set(AuthenticatedSession session) {
        CURRENT.set(session);
    }

    static void clear() {
        CURRENT.remove();
    }
}
