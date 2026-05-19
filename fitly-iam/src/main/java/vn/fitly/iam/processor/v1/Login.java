/**
 * Project: Fitly Platform
 * Author:  fitly.zero
 * Date:    May 14, 2026
 * Time:    11:06:50 AM
 * * Copyright (c) 2026 fitly.zero. All rights reserved.
 * Licensed under the Apache License 2.0.
 */
package vn.fitly.iam.processor.v1;

import java.util.UUID;

import org.mindrot.jbcrypt.BCrypt;

import vn.fitly.common.exception.ErrorStatus;
import vn.fitly.common.exception.FitlyBussinessException;
import vn.fitly.common.exception.FitlyRuntimeException;
import vn.fitly.common.language.DefaultSystemMessage;
import vn.fitly.common.utils.StringUtils;
import vn.fitly.foundation.dao.DaoFactory;
import vn.fitly.foundation.processor.AFitlyProcessor;
import vn.fitly.iam.dao.RoleDao;
import vn.fitly.iam.dao.UserDao;
import vn.fitly.iam.model.User;
import vn.fitly.iam.request.LoginRequest;
import vn.fitly.iam.response.LoginResponse;

/**
 * 
 */
public class Login extends AFitlyProcessor<LoginRequest, LoginResponse> {

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
    protected LoginResponse processInternal() throws Exception {

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

        String token = generateJwtToken(user);
        String refreshToken = generateRefreshToken(user);

        LoginResponse response = new LoginResponse();
        response.setAccessToken(token);
        response.setRefreshToken(refreshToken);
        response.setUserId(user.getUserId());

        RoleDao roleDao = DaoFactory.getDao(RoleDao.class);

        response.setPositionList(roleDao.getUserPositions(user.getUserId()));

        return response;
    }

    private String generateJwtToken(User user) {
        // Note: For production, integrate with a real JWT library like jjwt or
        // nimbus-jose-jwt
        return "mock-jwt-token-for-user-" + user.getUsername() + "-" + UUID.randomUUID().toString();
    }

    private String generateRefreshToken(User user) {
        // Note: For production, this should be a secure, long-lived token,
        // often stored in the database to allow revocation.
        return "mock-refresh-token-for-user-" + user.getUsername() + "-" + UUID.randomUUID().toString();
    }
}
