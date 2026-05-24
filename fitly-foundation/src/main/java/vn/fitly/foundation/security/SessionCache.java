package vn.fitly.foundation.security;

import java.time.Duration;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;

public final class SessionCache {

    private static final Cache<String, AuthenticatedSession> CACHE = Caffeine.newBuilder()
            .maximumSize(20000)
            .expireAfterWrite(Duration.ofMinutes(30))
            .build();

    private SessionCache() {
    }

    public static AuthenticatedSession get(String sessionTokenHash) {
        return CACHE.getIfPresent(sessionTokenHash);
    }

    public static void put(String sessionTokenHash, AuthenticatedSession session) {
        if (sessionTokenHash != null && session != null) {
            CACHE.put(sessionTokenHash, session);
        }
    }

    public static void remove(String sessionTokenHash) {
        if (sessionTokenHash != null) {
            CACHE.invalidate(sessionTokenHash);
        }
    }
}
