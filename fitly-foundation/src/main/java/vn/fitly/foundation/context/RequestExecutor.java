package vn.fitly.foundation.context;

import org.springframework.stereotype.Component;

import vn.fitly.common.exception.ErrorStatus;
import vn.fitly.common.exception.FitlyRuntimeException;
import vn.fitly.common.language.DefaultSystemMessage;
import vn.fitly.foundation.processor.AFitlyProcessor;
import vn.fitly.foundation.response.BaseResponse;

/**
 * Project: Fitly Platform Author: fitly.zero Date: 10/5/26 Time: 16:06 *
 * Copyright (c) 2026 fitlyzero. All rights reserved. Licensed under the Apache
 * License 2.0.
 */
@Component
public class RequestExecutor {

    public <RQ, RP> BaseResponse<RP> process(AFitlyProcessor<RQ, RP> processor) {
        return process(false, processor);
    }

    public <RQ, RP> BaseResponse<RP> process(boolean readOnly, AFitlyProcessor<RQ, RP> processor) {

        try {

            Ctx ctx = new Ctx(readOnly);

            try {

                RP result = ScopedValue.where(CtxRequest.CTX, ctx)
                        .call(() -> processor.process());
                ctx.commit();
                return BaseResponse.success(result);

            } catch (Exception e) {

                ctx.rollback();
                throw new FitlyRuntimeException(ErrorStatus.INTERNAL_ERROR,
                        DefaultSystemMessage.INTERNAL_ERROR.name(),
                        e);

            } finally {
                ctx.close();
            }

        } catch (Exception e) {

            throw new FitlyRuntimeException(ErrorStatus.INTERNAL_ERROR,
                    DefaultSystemMessage.INTERNAL_ERROR.name(),
                    e);
        }

    }

}
