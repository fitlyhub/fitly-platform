package vn.fitly.foundation.context;

import java.time.Duration;
import java.util.Enumeration;
import java.util.Map.Entry;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import vn.fitly.foundation.processor.IProcessor;
import vn.fitly.foundation.response.BaseResponse;
import vn.fitly.generated.context.FitlyCookie;

/**
 * Project: Fitly Platform Author: fitly.zero Date: 10/5/26 Time: 16:06 *
 * Copyright (c) 2026 fitlyzero. All rights reserved. Licensed under the Apache
 * License 2.0.
 */
@Component
public class RequestExecutor {

    public <RQ, RP> BaseResponse<RP> process(IProcessor<RQ, RP> processor) {

        ServletRequestAttributes attributes = getServletRequestAttributes();

        HttpServletRequest httpRequest = attributes.getRequest();

        FitlyContext ctx = new FitlyContext(createContext(httpRequest));

        try {

            RP data = Ctx.call(ctx, processor);

            return BaseResponse.success(data);

        } finally {

            HttpServletResponse httpResponse = attributes.getResponse();
            flushResponse(httpResponse, ctx.http());

        }
    }

    private ServletRequestAttributes getServletRequestAttributes() {

        if (!(RequestContextHolder.getRequestAttributes() instanceof ServletRequestAttributes attributes)) {
            throw new IllegalStateException("Current request is not a servlet request");
        }

        return attributes;
    }

    private HttpContext createContext(HttpServletRequest request) {

        HttpContext context = new HttpContext();

        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            return context;
        }

        for (Cookie cookie : cookies) {
            context.putRequestCookie(cookie.getName(), cookie.getValue());
        }

        Enumeration<String> headers = request.getHeaderNames();

        while (headers.hasMoreElements()) {
            String name = headers.nextElement();
            context.putRequestHeader(name, request.getHeader(name));
        }

        return context;
    }

    private void flushResponse(HttpServletResponse response, HttpContext context) {

        for (FitlyCookie fitlyCookie : context.getResponseCookieList()) {
            ResponseCookie cookie = ResponseCookie.from(
                    fitlyCookie.getName(),
                    fitlyCookie.getValue())
                    .httpOnly(fitlyCookie.isHttpOnly())
                    .secure(fitlyCookie.isSecure())
                    .path(fitlyCookie.getPath())
                    .maxAge(Duration.ofSeconds(fitlyCookie.getMaxAgeSeconds()))
                    .build();

            response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
        }

        for (Entry<String, String> header : context.getResponseHeaderMap().entrySet()) {
            response.addHeader(header.getKey(), header.getValue());
        }
    }

}
