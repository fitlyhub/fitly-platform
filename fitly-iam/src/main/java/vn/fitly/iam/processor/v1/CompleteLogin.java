/**
 * Project: Fitly Platform
 * Author:  fitly.zero
 * Date:    May 23, 2026
 * Time:    11:52:12 AM
 * * Copyright (c) 2026 fitly.zero. All rights reserved.
 * Licensed under the Apache License 2.0.
 */
package vn.fitly.iam.processor.v1;

import vn.fitly.common.exception.FitlyBussinessException;
import vn.fitly.foundation.context.Ctx;
import vn.fitly.foundation.dao.DaoFactory;
import vn.fitly.foundation.processor.BaseProcessor;
import vn.fitly.iam.cache.LoginSessionCache;
import vn.fitly.iam.dao.PositionDao;
import vn.fitly.iam.model.Position;
import vn.fitly.iam.request.CompleteLoginRequest;
import vn.fitly.iam.response.LoginResponse;

/**
 * 
 */
public class CompleteLogin extends BaseProcessor<CompleteLoginRequest, LoginResponse> {

    /**
     * @param request
     */
    public CompleteLogin(CompleteLoginRequest request) {
        super(request);
    }

    @Override
    protected void validate() throws Exception {

    }

    @Override
    protected LoginResponse doProcess() throws Exception {

        String loginSession = Ctx.http().getCookie("FITLY_LOGIN_SESSION");

        if (loginSession == null) {
            // TODO

        }

        LoginResponse pendingLogin = new LoginSessionCache(loginSession).get();
        if (pendingLogin == null) {
            // TODO
            throw new FitlyBussinessException(null, loginSession);
        }

        Position choosedPosition = null;
        for (Position position : pendingLogin.getPositionList()) {
            if (position.getPositionId().equals(request.getPositionId())) {
                choosedPosition = position;
                break;
            }
        }

        if (choosedPosition == null) {
            throw new FitlyBussinessException(null, loginSession);
        }

        PositionDao roleDao = DaoFactory.getDao(PositionDao.class);
        if (roleDao.isValidUserRole(pendingLogin.getUserId(), choosedPosition)) {
            return new LoginResponse(pendingLogin.getUserId(), choosedPosition, pendingLogin.getTraceId());
        }
        
        throw new FitlyBussinessException(null, loginSession);

    }

}
