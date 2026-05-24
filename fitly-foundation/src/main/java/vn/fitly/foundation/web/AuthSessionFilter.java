package vn.fitly.foundation.web;

import java.io.IOException;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import tools.jackson.databind.ObjectMapper;
import vn.fitly.common.json.JsonMapperBuilder;
import vn.fitly.foundation.response.BaseResponse;
import vn.fitly.foundation.security.AuthenticatedSession;
import vn.fitly.foundation.security.SessionCookieManager;
import vn.fitly.foundation.security.SessionHash;
import vn.fitly.foundation.security.SessionJdbcValidator;

@Component
public class AuthSessionFilter extends OncePerRequestFilter {

    private static final ObjectMapper MAPPER = JsonMapperBuilder.get();

    private final SessionCookieManager cookieManager;

    public AuthSessionFilter(SessionCookieManager cookieManager) {
        this.cookieManager = cookieManager;
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();
        return "OPTIONS".equalsIgnoreCase(request.getMethod())
                || path.equals("/api/auth/login")
                || path.equals("/api/auth/logout")
                || path.equals("/auth/v1/login")
                || path.startsWith("/actuator")
                || path.equals("/error");
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        try {
            String sessionSecret = cookieManager.read(request);
            String sessionHash = SessionHash.hash(sessionSecret);
            AuthenticatedSession session = SessionJdbcValidator.findValidSession(sessionHash);
            if (session == null) {
                writeUnauthorized(response, "SESSION_INVALID");
                return;
            }

            AuthenticatedRequest.set(session);
            filterChain.doFilter(request, response);
        } catch (Exception e) {
            writeUnauthorized(response, "SESSION_INVALID");
        } finally {
            AuthenticatedRequest.clear();
        }
    }

    private void writeUnauthorized(HttpServletResponse response, String message) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        MAPPER.writeValue(response.getWriter(), BaseResponse.error(message));
    }
}
