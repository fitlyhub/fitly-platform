/**
 * Project: Fitly Platform
 * Author:  fitly.zero
 * Date:    May 22, 2026
 * Time:    11:45:00 AM
 * Copyright (c) 2026 fitly.zero. All rights reserved.
 * Licensed under the Apache License 2.0.
 */
package vn.fitly.foundation.security;

import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import redis.clients.jedis.RedisClient;
import redis.clients.jedis.params.SetParams;
import vn.fitly.common.json.JsonMapperBuilder;
import vn.fitly.common.utils.StringUtils;
import vn.fitly.infrastructure.datasource.tenant.DBProvider;

/**
 * Stores auth sessions in Redis when available, with an in-memory fallback for
 * local/dev runtime.
 */
final class AuthSessionStore {

    private static final String SERVICE_NAME = "cache";
    private static final String SESSION_KEY_PREFIX = "auth:session:";
    private static final String REFRESH_KEY_PREFIX = "auth:refresh:";

    private static final Map<String, AuthSession> LOCAL_SESSION_CACHE = new ConcurrentHashMap<>();
    private static final Map<String, String> LOCAL_REFRESH_CACHE = new ConcurrentHashMap<>();

    private AuthSessionStore() {
    }

    static void save(AuthSession session, long ttlSeconds) {
        RedisClient redisClient = getRedisClient();
        if (redisClient != null) {
            redisClient.set(sessionKey(session.getSessionId()), JsonMapperBuilder.get().writeValueAsString(session),
                    SetParams.setParams().ex(ttlSeconds));
            redisClient.set(refreshKey(session.getRefreshToken()), session.getSessionId(),
                    SetParams.setParams().ex(ttlSeconds));
            return;
        }

        LOCAL_SESSION_CACHE.put(session.getSessionId(), session);
        LOCAL_REFRESH_CACHE.put(session.getRefreshToken(), session.getSessionId());
    }

    static AuthSession findByRefreshToken(String refreshToken) {
        if (StringUtils.isBlank(refreshToken)) {
            return null;
        }

        RedisClient redisClient = getRedisClient();
        if (redisClient != null) {
            String sessionId = redisClient.get(refreshKey(refreshToken));
            if (StringUtils.isBlank(sessionId)) {
                return null;
            }

            return findBySessionId(sessionId);
        }

        return findBySessionId(LOCAL_REFRESH_CACHE.get(refreshToken));
    }

    static AuthSession findBySessionId(String sessionId) {
        if (StringUtils.isBlank(sessionId)) {
            return null;
        }

        RedisClient redisClient = getRedisClient();
        AuthSession session;
        if (redisClient != null) {
            String value = redisClient.get(sessionKey(sessionId));
            if (StringUtils.isBlank(value)) {
                return null;
            }

            session = JsonMapperBuilder.get().readValue(value, AuthSession.class);
        } else {
            session = LOCAL_SESSION_CACHE.get(sessionId);
        }

        if (isExpired(session)) {
            revoke(session);
            return null;
        }

        return session;
    }

    static void revokeByRefreshToken(String refreshToken) {
        AuthSession session = findByRefreshToken(refreshToken);
        revoke(session);
    }

    static void revokeBySessionId(String sessionId) {
        AuthSession session = findBySessionId(sessionId);
        revoke(session);
    }

    private static void revoke(AuthSession session) {
        if (session == null) {
            return;
        }

        RedisClient redisClient = getRedisClient();
        if (redisClient != null) {
            redisClient.del(sessionKey(session.getSessionId()));
            redisClient.del(refreshKey(session.getRefreshToken()));
            return;
        }

        LOCAL_SESSION_CACHE.remove(session.getSessionId());
        LOCAL_REFRESH_CACHE.remove(session.getRefreshToken());
    }

    private static boolean isExpired(AuthSession session) {
        return session == null || session.isRevoked() || session.getExpiresAt() == null
                || !session.getExpiresAt().isAfter(Instant.now());
    }

    private static RedisClient getRedisClient() {
        try {
            return DBProvider.getRedisClient(SERVICE_NAME);
        } catch (Exception e) {
            return null;
        }
    }

    private static String sessionKey(String sessionId) {
        return SESSION_KEY_PREFIX + sessionId;
    }

    private static String refreshKey(String refreshToken) {
        return REFRESH_KEY_PREFIX + refreshToken;
    }
}
