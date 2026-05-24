/**
 * Project: Fitly Platform
 * Author:  fitly.zero
 * Date:    May 21, 2026
 * Time:    2:00:00 PM
 * Copyright (c) 2026 fitly.zero. All rights reserved.
 * Licensed under the Apache License 2.0.
 */
package vn.fitly.foundation.security;

import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.time.Instant;
import java.util.Date;
import java.util.UUID;

import javax.crypto.SecretKey;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import vn.fitly.common.exception.ErrorStatus;
import vn.fitly.common.exception.FitlyBussinessException;
import vn.fitly.common.json.JsonMapperBuilder;
import vn.fitly.common.utils.DateTimeUtils;
import vn.fitly.common.utils.StringUtils;
import vn.fitly.infrastructure.config.ApplicationConfig;
import vn.fitly.infrastructure.spring.FitlyJwtConfig;

/**
 * Utility for creating and parsing user JWT access tokens and refresh tokens.
 */
public class JwtTokenService {

    private static final String TOKEN_TYPE_CLAIM = "token_type";

    private static final String ACCESS_TOKEN_TYPE = "ACCESS";

    private static final SecureRandom SECURE_RANDOM = new SecureRandom();

    private JwtTokenService() {
    }

    public static AuthTokenPair issueTokens(AccessToken token) {
        
        validateTokenUser(token);

        String sessionId = generateUuidV7();
        String refreshToken = generateUuidV7();
        token.setSessionId(sessionId);

        long refreshTtlSeconds = getJwtConfig().getRefreshTokenExpireMinutes() * 60;
        Instant refreshExpiresAt = DateTimeUtils.now().plusSeconds(refreshTtlSeconds);
        AuthSession session = new AuthSession(sessionId, refreshToken, refreshExpiresAt, token);
        AuthSessionStore.save(session, refreshTtlSeconds);

        String accessToken = generateAccessToken(token);
        return new AuthTokenPair(sessionId, accessToken, refreshToken);
    }

    public static String generateAccessToken(AccessToken token) {
        validateTokenUser(token);
        validateSessionId(token.getSessionId());
        return generateAccessTokenJwt(token);
    }

    public static String generateRefreshToken(AccessToken token) {
        return issueTokens(token).getRefreshToken();
    }

    public static AccessToken verifyAccessToken(String accessToken) {
        return parseAccessToken(accessToken);
    }

    public static AccessToken verifyRefreshToken(String refreshToken) {
        AuthSession session = AuthSessionStore.findByRefreshToken(refreshToken);
        if (session == null) {
            throw new FitlyBussinessException(ErrorStatus.UNAUTHORIZED, "REFRESH_TOKEN_INVALID");
        }

        return session.toAccessToken();
    }

    public static String refreshAccessToken(String refreshToken) {
        AccessToken token = verifyRefreshToken(refreshToken);
        return generateAccessToken(token);
    }

    public static String refreshToken(String refreshToken) {
        return refreshAccessToken(refreshToken);
    }

    public static AccessToken parseAccessToken(String accessToken) {
        return parseToken(accessToken, ACCESS_TOKEN_TYPE, "ACCESS_TOKEN_USER_ID_MISSING");
    }

    public static AccessToken parseRefreshToken(String refreshToken) {
        return verifyRefreshToken(refreshToken);
    }

    public static void revokeRefreshToken(String refreshToken) {
        AuthSessionStore.revokeByRefreshToken(refreshToken);
    }

    public static void revokeSession(String sessionId) {
        AuthSessionStore.revokeBySessionId(sessionId);
    }

    private static String generateAccessTokenJwt(AccessToken token) {

        Instant now = DateTimeUtils.now();
        FitlyJwtConfig config = getJwtConfig();
        Instant expiresAt = now.plusSeconds(config.getAccessTokenExpireMinutes() * 60);

        return Jwts.builder()
                .subject(token.toString())
                .claim(TOKEN_TYPE_CLAIM, ACCESS_TOKEN_TYPE)
                .issuedAt(Date.from(now))
                .expiration(Date.from(expiresAt))
                .signWith(getSigningKey(config))
                .compact();
    }

    private static AccessToken parseToken(String token, String expectedType, String missingUserMessage) {

        Claims claims = parseClaims(token, expectedType);
        String subjectJson = claims.getSubject();
        if (StringUtils.isBlank(subjectJson)) {
            throw new FitlyBussinessException(ErrorStatus.UNAUTHORIZED, missingUserMessage);
        }

        try {
            AccessToken accessToken = JsonMapperBuilder.get().readValue(subjectJson, AccessToken.class);
            if (accessToken == null || accessToken.getUserId() == null) {
                throw new FitlyBussinessException(ErrorStatus.UNAUTHORIZED, missingUserMessage);
            }
            validateSessionId(accessToken.getSessionId());

            return accessToken;
        } catch (FitlyBussinessException e) {
            throw e;
        } catch (Exception e) {
            throw new FitlyBussinessException(ErrorStatus.UNAUTHORIZED, "TOKEN_INVALID");
        }
    }

    private static Claims parseClaims(String token, String expectedType) {
        String normalizedToken = normalizeBearerToken(token);

        try {
            Claims claims = Jwts.parser()
                    .verifyWith(getSigningKey(getJwtConfig()))
                    .build()
                    .parseSignedClaims(normalizedToken)
                    .getPayload();

            String tokenType = claims.get(TOKEN_TYPE_CLAIM, String.class);
            if (!expectedType.equals(tokenType)) {
                throw new FitlyBussinessException(ErrorStatus.UNAUTHORIZED, "INVALID_TOKEN_TYPE");
            }

            return claims;
        } catch (FitlyBussinessException e) {
            throw e;
        } catch (Exception e) {
            throw new FitlyBussinessException(ErrorStatus.UNAUTHORIZED, "TOKEN_INVALID");
        }
    }

    private static String normalizeBearerToken(String token) {
        if (StringUtils.isBlank(token)) {
            throw new FitlyBussinessException(ErrorStatus.UNAUTHORIZED, "TOKEN_MISSING");
        }

        String normalizedToken = token.trim();
        if (!normalizedToken.regionMatches(true, 0, "Bearer ", 0, 7)) {
            return normalizedToken;
        }

        return normalizedToken.substring(7).trim();
    }

    private static void validateTokenUser(AccessToken token) {
        if (token == null || token.getUserId() == null || StringUtils.isBlank(token.getUsername())) {
            throw new FitlyBussinessException(ErrorStatus.UNAUTHORIZED, "TOKEN_USER_MISSING");
        }
    }

    private static void validateSessionId(String sessionId) {
        if (StringUtils.isBlank(sessionId)) {
            throw new FitlyBussinessException(ErrorStatus.UNAUTHORIZED, "TOKEN_SESSION_MISSING");
        }
    }

    private static String generateUuidV7() {
        long timestampMillis = DateTimeUtils.now().toEpochMilli();

        long mostSignificantBits = (timestampMillis & 0xFFFFFFFFFFFFL) << 16;
        mostSignificantBits |= 0x7000L;
        mostSignificantBits |= SECURE_RANDOM.nextInt(1 << 12);

        long leastSignificantBits = SECURE_RANDOM.nextLong();
        leastSignificantBits &= 0x3FFFFFFFFFFFFFFFL;
        leastSignificantBits |= 0x8000000000000000L;

        return new UUID(mostSignificantBits, leastSignificantBits).toString();
    }

    private static FitlyJwtConfig getJwtConfig() {
        FitlyJwtConfig config = ApplicationConfig.getJwtConfig();
        if (config == null) {
            throw new FitlyBussinessException(ErrorStatus.UNAUTHORIZED, "JWT_CONFIG_MISSING");
        }

        if (StringUtils.isBlank(config.getSecret())) {
            throw new FitlyBussinessException(ErrorStatus.UNAUTHORIZED, "JWT_SECRET_MISSING");
        }

        return config;
    }

    private static SecretKey getSigningKey(FitlyJwtConfig config) {
        return Keys.hmacShaKeyFor(config.getSecret().getBytes(StandardCharsets.UTF_8));
    }
}
