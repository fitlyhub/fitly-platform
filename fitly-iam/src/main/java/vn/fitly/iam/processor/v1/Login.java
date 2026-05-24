/**
 * Project: Fitly Platform
 * Author:  fitly.zero
 * Date:    May 14, 2026
 * Time:    11:06:50 AM
 * * Copyright (c) 2026 fitly.zero. All rights reserved.
 * Licensed under the Apache License 2.0.
 */
package vn.fitly.iam.processor.v1;

import java.util.List;

import org.mindrot.jbcrypt.BCrypt;

import vn.fitly.common.exception.ErrorStatus;
import vn.fitly.common.exception.FitlyBussinessException;
import vn.fitly.common.language.DefaultSystemMessage;
import vn.fitly.common.utils.StringUtils;
import vn.fitly.foundation.context.Ctx;
import vn.fitly.foundation.dao.DaoFactory;
import vn.fitly.foundation.processor.BaseProcessor;
import vn.fitly.foundation.security.SessionHelper;
import vn.fitly.iam.cache.LoginSessionCache;
import vn.fitly.iam.dao.PositionDao;
import vn.fitly.iam.dao.UserDao;
import vn.fitly.iam.model.Position;
import vn.fitly.iam.model.User;
import vn.fitly.iam.request.LoginRequest;
import vn.fitly.iam.response.LoginResponse;

/**
 * 
 */
public class Login extends BaseProcessor<LoginRequest, LoginResponse> {

    public Login(LoginRequest request) {
        super(request);
    }

    @Override
    protected void validate() throws Exception {

        if (request == null) {
            throw new FitlyBussinessException(ErrorStatus.REQUEST_INVALID, DefaultSystemMessage.REQUEST_INVALID.name());
        }

        if (StringUtils.isBlank(request.getUsername()) || StringUtils.isBlank(request.getPassword())) {
            throw new FitlyBussinessException(ErrorStatus.REQUEST_INVALID, DefaultSystemMessage.REQUEST_INVALID.name());
        }

    }

    @Override
    protected LoginResponse doProcess() throws Exception {

        UserDao userDao = DaoFactory.getDao(UserDao.class);
        User user = userDao.getUserByUsername(request.getUsername());

        if (user == null) {
            throw new FitlyBussinessException(ErrorStatus.NOTFOUND, "USER_NOT_FOUND");
        }

        if (!user.isActive()) {
            throw new FitlyBussinessException(ErrorStatus.RULE_EXCEPTION, "USER_DEACTIVE");

        }

        if (!BCrypt.checkpw(request.getPassword(), user.getPassword())) {
            throw new FitlyBussinessException(ErrorStatus.UNAUTHORIZED, "USER_UNAUTHORIZED");
        }

        PositionDao roleDao = DaoFactory.getDao(PositionDao.class);
        List<Position> positionList = roleDao.getUserPositions(user.getUserId());
        if (positionList.isEmpty()) {
            throw new FitlyBussinessException(ErrorStatus.RULE_EXCEPTION, "POSITION_NOT_EXIST");
        }

        String loginSession = SessionHelper.generateSession();

        LoginResponse response = new LoginResponse(user.getUserId(), positionList, request.getTraceId());

        new LoginSessionCache(loginSession).put(response);

        Ctx.http().setCookie("FITLY_LOGIN_SESSION", loginSession, 30);

        return response;
    }

}
