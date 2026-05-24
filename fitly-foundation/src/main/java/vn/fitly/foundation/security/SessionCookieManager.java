package vn.fitly.foundation.security;

import java.time.Duration;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import vn.fitly.common.utils.StringUtils;

@Component
public class SessionCookieManager {

    private final SessionCookieProperties properties;

    public SessionCookieManager(SessionCookieProperties properties) {
        this.properties = properties;
    }

    public String read(HttpServletRequest request) {
        if (request == null || request.getCookies() == null) {
            return null;
        }

        for (Cookie cookie : request.getCookies()) {
            if (properties.getCookieName().equals(cookie.getName())) {
                return cookie.getValue();
            }
        }

        return null;
    }

    public void set(HttpServletResponse response, String sessionSecret) {
        if (response == null || StringUtils.isBlank(sessionSecret)) {
            return;
        }

        ResponseCookie cookie = baseCookie(sessionSecret)
                .maxAge(Duration.ofMinutes(properties.getExpireMinutes()))
                .build();
        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
    }

    public void clear(HttpServletResponse response) {
        if (response == null) {
            return;
        }

        ResponseCookie cookie = baseCookie("")
                .maxAge(Duration.ZERO)
                .build();
        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
    }

    public String getCookieName() {
        return properties.getCookieName();
    }

    private ResponseCookie.ResponseCookieBuilder baseCookie(String value) {
        return ResponseCookie.from(properties.getCookieName(), value)
                .httpOnly(properties.isCookieHttpOnly())
                .secure(properties.isCookieSecure())
                .sameSite(properties.getCookieSameSite())
                .path(properties.getCookiePath());
    }
}
