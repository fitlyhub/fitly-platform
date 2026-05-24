package vn.fitly.foundation.controller;

import vn.fitly.foundation.context.RequestExecutor;
import vn.fitly.foundation.processor.IProcessor;
import vn.fitly.foundation.response.BaseResponse;

public abstract class FitlyController{

    private final RequestExecutor executor;

    protected FitlyController(RequestExecutor executor) {
        this.executor = executor;
    }

    protected <RQ, RP> BaseResponse<RP> execute(IProcessor<RQ, RP> processor) {
        return executor.process(processor);
    }

}
