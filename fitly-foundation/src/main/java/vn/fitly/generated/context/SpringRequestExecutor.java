package vn.fitly.generated.context;

import java.time.Duration;
import java.util.Enumeration;
import java.util.UUID;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import vn.fitly.foundation.context.FitlyContext;
import vn.fitly.foundation.context.HttpContext;
import vn.fitly.foundation.processor.BaseProcessor;
import vn.fitly.foundation.response.BaseResponse;

@Component
public class SpringRequestExecutor {

    private static final String HEADER_X_FORWARDED_FOR = "X-Forwarded-For";

    private static final String HEADER_X_REAL_IP = "X-Real-IP";

    private static final String HEADER_X_TRACE_ID = "X-Trace-Id";

    private static final String HEADER_X_REQUEST_ID = "X-Request-Id";

    private final FitlySessionConfig config;

    public SpringRequestExecutor(FitlySessionConfig config) {
        this.config = config;
    }

    public <RQ, RP> BaseResponse<RP> process(BaseProcessor<RQ, RP> processor) {
        ServletRequestAttributes attributes = getServletRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        HttpServletResponse response = attributes.getResponse();

        if (response == null) {
            throw new IllegalStateException("HttpServletResponse is not available");
        }

        FitlyContext context = createContext(request);

        try {
            RP data = ContextHolder.call(context, processor::process);
            return BaseResponse.success(data);
        } catch (Exception e) {
            // TODO BaseResponse currently exposes error(String), not error(Exception).
            return BaseResponse.error(e.getMessage());
        } finally {
            flushResponse(response, context);
        }
    }

    private ServletRequestAttributes getServletRequestAttributes() {
        if (!(RequestContextHolder.getRequestAttributes()
                instanceof ServletRequestAttributes attributes)) {
            throw new IllegalStateException("Current request is not a servlet request");
        }
        return attributes;
    }

    private FitlyContext createContext(HttpServletRequest request) {
        HttpContext httpContext = createHttpContext(request);
        BusinessContext businessContext = new BusinessContext();
        DbContext dbContext = new DbContext();
        TraceContext traceContext = createTraceContext(request);

        return new FitlyContext(
                httpContext,
                businessContext,
                dbContext,
                traceContext);
    }

    private HttpContext createHttpContext(HttpServletRequest request) {
        HttpContext context = new HttpContext();
        context.setMethod(request.getMethod());
        context.setPath(request.getRequestURI());
        context.setQueryString(request.getQueryString());
        context.setContentType(request.getContentType());
        context.setAccept(request.getHeader(HttpHeaders.ACCEPT));
        context.setUserAgent(request.getHeader(HttpHeaders.USER_AGENT));
        context.setOrigin(request.getHeader(HttpHeaders.ORIGIN));
        context.setReferer(request.getHeader(HttpHeaders.REFERER));
        context.setClientIp(resolveClientIp(request));

        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames != null && headerNames.hasMoreElements()) {
            String name = headerNames.nextElement();
            context.putRequestHeader(name, request.getHeader(name));
        }

        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                context.putRequestCookie(cookie.getName(), cookie.getValue());
            }
        }

        return context;
    }

    private TraceContext createTraceContext(HttpServletRequest request) {
        String traceId = firstNotBlank(
                request.getHeader(HEADER_X_TRACE_ID),
                UUID.randomUUID().toString());
        String requestId = firstNotBlank(
                request.getHeader(HEADER_X_REQUEST_ID),
                traceId);

        return new TraceContext(
                traceId,
                requestId,
                System.currentTimeMillis(),
                request.getRequestURI(),
                request.getMethod());
    }

    private String resolveClientIp(HttpServletRequest request) {
        String forwardedFor = request.getHeader(HEADER_X_FORWARDED_FOR);
        if (!isBlank(forwardedFor)) {
            return forwardedFor.split(",", 2)[0].trim();
        }

        String realIp = request.getHeader(HEADER_X_REAL_IP);
        if (!isBlank(realIp)) {
            return realIp.trim();
        }

        return request.getRemoteAddr();
    }

    private void flushResponse(
            HttpServletResponse response,
            FitlyContext context) {
        flushResponseHeaders(response, context.http());
        flushResponseCookies(response, context.http());
    }

    private void flushResponseHeaders(
            HttpServletResponse response,
            HttpContext context) {
        for (var entry : context.getResponseHeaders().entrySet()) {
            response.setHeader(entry.getKey(), entry.getValue());
        }
    }

    private void flushResponseCookies(
            HttpServletResponse response,
            HttpContext context) {
        for (FitlyCookie fitlyCookie : context.getResponseCookies()) {
            ResponseCookie.ResponseCookieBuilder builder = ResponseCookie.from(
                    fitlyCookie.name(),
                    fitlyCookie.value())
                    .httpOnly(config.isCookieHttpOnly())
                    .secure(config.isCookieSecure())
                    .sameSite(config.getCookieSameSite())
                    .path(fitlyCookie.path())
                    .maxAge(Duration.ofSeconds(fitlyCookie.maxAgeSeconds()));

            if (!isBlank(config.getCookieDomain())) {
                builder.domain(config.getCookieDomain());
            }

            response.addHeader(HttpHeaders.SET_COOKIE, builder.build().toString());
        }
    }

    private String firstNotBlank(String value, String fallback) {
        return isBlank(value) ? fallback : value;
    }

    private boolean isBlank(String value) {
        return value == null || value.isBlank();
    }

}
