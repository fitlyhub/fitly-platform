package vn.fitly.generated.context;

public class TraceContext {

    private String traceId;

    private String requestId;

    private long startTimeMillis;

    private String apiPath;

    private String httpMethod;

    public TraceContext() {
    }

    public TraceContext(
            String traceId,
            String requestId,
            long startTimeMillis,
            String apiPath,
            String httpMethod) {
        this.traceId = traceId;
        this.requestId = requestId;
        this.startTimeMillis = startTimeMillis;
        this.apiPath = apiPath;
        this.httpMethod = httpMethod;
    }

    public String getTraceId() {
        return traceId;
    }

    public void setTraceId(String traceId) {
        this.traceId = traceId;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public long getStartTimeMillis() {
        return startTimeMillis;
    }

    public void setStartTimeMillis(long startTimeMillis) {
        this.startTimeMillis = startTimeMillis;
    }

    public String getApiPath() {
        return apiPath;
    }

    public void setApiPath(String apiPath) {
        this.apiPath = apiPath;
    }

    public String getHttpMethod() {
        return httpMethod;
    }

    public void setHttpMethod(String httpMethod) {
        this.httpMethod = httpMethod;
    }

}
