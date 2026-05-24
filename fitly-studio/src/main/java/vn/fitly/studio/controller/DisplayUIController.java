/**
 * Project: Fitly Platform
 * Author:  fitly.zero
 * Date:    May 21, 2026
 * Time:    10:19:52 AM
 * Copyright (c) 2026 fitly.zero. All rights reserved.
 * Licensed under the Apache License 2.0.
 */
package vn.fitly.studio.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import vn.fitly.foundation.context.RequestExecutor;
import vn.fitly.foundation.request.ARequest;
import vn.fitly.foundation.response.BaseResponse;
import vn.fitly.studio.processor.v1.MainDisplay;
import vn.fitly.studio.processor.v1.MainDisplayResponse;

/**
 * REST Controller for Display-related UI operations.
 */
@RestController
@RequestMapping("/ui/display")
public class DisplayUIController {

    private final RequestExecutor executor;

    @Autowired
    public DisplayUIController(RequestExecutor executor) {
        this.executor = executor;
    }

    @PostMapping("/load")
    public BaseResponse<MainDisplayResponse> load(@RequestBody ARequest request) {
        return executor.process(new MainDisplay(request));
    }
}
