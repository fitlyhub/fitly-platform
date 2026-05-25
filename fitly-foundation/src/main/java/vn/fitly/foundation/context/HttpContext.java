package vn.fitly.foundation.context;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class HttpContext {

    private final Map<String, String> requestHeaderMap = new HashMap<>();

    private final Map<String, String> requestCookieMap = new HashMap<>();

    private final Map<String, String> responseHeaderMap = new HashMap<>();

    private final List<FitlyCookie> responseCookieList = new ArrayList<>();

    String getHeader(String name) {
        return requestHeaderMap.get(normalizeHeaderName(name));
    }

    public String getCookie(String name) {
        return requestCookieMap.get(name);
    }
    
    void putRequestHeader(String name, String value) {
        requestHeaderMap.put(name.toLowerCase(), value);
    }

    void putRequestCookie(String name, String value) {
        requestCookieMap.put(name, value);
    }


    public void setHeader(String name, String value) {
        responseHeaderMap.put(normalizeHeaderName(name), value);
    }

    public void setCookie(String name, String value, long maxAgeSeconds) {
        responseCookieList.add(new FitlyCookie(name, value, maxAgeSeconds));
    }

    public Map<String, String> getResponseHeaderMap() {
        return responseHeaderMap;
    }

    public List<FitlyCookie> getResponseCookieList() {
        return responseCookieList;
    }

    private String normalizeHeaderName(String name) {
        if (name == null) {
            return null;
        }
        return name.toLowerCase(Locale.ROOT);
    }
    
    
}
