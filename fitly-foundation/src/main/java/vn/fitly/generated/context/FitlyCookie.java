package vn.fitly.generated.context;

public class FitlyCookie {

    private final String name;
    private final String value;
    private final String path;
    private final long maxAgeSeconds;
    private final boolean httpOnly = false;
    private final boolean secure = true;

    public FitlyCookie(String name, String value, long maxAgeSeconds) {
        this.name = name;
        this.value = value;
        this.path = "/";
        this.maxAgeSeconds = maxAgeSeconds;
    }

    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }

    public String getPath() {
        return path;
    }

    public long getMaxAgeSeconds() {
        return maxAgeSeconds;
    }

    public boolean isHttpOnly() {
        return httpOnly;
    }

    public boolean isSecure() {
        return secure;
    }

}
