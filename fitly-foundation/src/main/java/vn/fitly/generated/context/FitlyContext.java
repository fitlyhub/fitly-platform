package vn.fitly.generated.context;

import vn.fitly.generated.context.TraceContext;

public class FitlyContext {

    private final HttpContext httpContext;

    private final BusinessContext businessContext;

    private final DbContext dbContext;

    private final TraceContext traceContext;

    public FitlyContext(
            HttpContext httpContext,
            BusinessContext businessContext,
            DbContext dbContext,
            TraceContext traceContext) {
        this.httpContext = httpContext;
        this.businessContext = businessContext;
        this.dbContext = dbContext;
        this.traceContext = traceContext;
    }

    public HttpContext http() {
        return httpContext;
    }

    public BusinessContext business() {
        return businessContext;
    }

    public DbContext db() {
        return dbContext;
    }

    public TraceContext trace() {
        return traceContext;
    }

}
