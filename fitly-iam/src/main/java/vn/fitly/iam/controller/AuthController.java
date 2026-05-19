/**
 * Project: Fitly Platform
 * Author:  fitly.zero
 * Date:    May 8, 2026
 * Time:    11:49:49 AM
 * * Copyright (c) 2026 fitly.zero. All rights reserved.
 * Licensed under the Apache License 2.0.
 */
package vn.fitly.iam.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import vn.fitly.foundation.context.RequestExecutor;
import vn.fitly.foundation.response.BaseResponse;
import vn.fitly.iam.processor.v1.Login;
import vn.fitly.iam.request.LoginRequest;
import vn.fitly.iam.response.LoginResponse;

/**
 * 
 */
@RestController
@RequestMapping("/users/v1")
public class AuthController {

    @Autowired
    RequestExecutor executor;

    @PostMapping("/login")
    public BaseResponse<LoginResponse> login(@RequestBody LoginRequest request) {

        return executor.process(new Login(request));
    }
}
