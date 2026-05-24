/**
 * Project: Fitly Platform
 * Author:  fitly.zero
 * Date:    May 14, 2026
 * Time:    11:06:50 AM
 * Copyright (c) 2026 fitly.zero. All rights reserved.
 * Licensed under the Apache License 2.0.
 */
package vn.fitly.studio.processor.v1;

import java.util.List;
import java.util.HashSet;
import java.util.Set;

import vn.fitly.common.exception.ErrorStatus;
import vn.fitly.common.exception.FitlyBussinessException;
import vn.fitly.common.language.DefaultSystemMessage;
import vn.fitly.common.utils.StringUtils;
import vn.fitly.foundation.context.SessionContext;
import vn.fitly.foundation.context.Ctx;
import vn.fitly.foundation.dao.DaoFactory;
import vn.fitly.foundation.dto.UserPrincipal;
import vn.fitly.foundation.processor.BaseProcessor;
import vn.fitly.foundation.request.ARequest;
import vn.fitly.studio.dao.MenuDao;
import vn.fitly.studio.dto.HeaderDto;
import vn.fitly.studio.dto.MenuDto;
import vn.fitly.studio.service.MenuService;


/**
 * Processor to build Main Display UI dashboard metadata.
 */
public class MainDisplay extends BaseProcessor<ARequest, MainDisplayResponse> {

    public MainDisplay(ARequest request) {
        super(request);
    }

    @Override
    protected void validate() throws Exception {
        if (request == null) {
            throw new FitlyBussinessException(ErrorStatus.REQUEST_INVALID, DefaultSystemMessage.REQUEST_INVALID.name());
        }

    }

    @Override
    protected MainDisplayResponse doProcess() throws Exception {
        
        MenuDao menuDao = DaoFactory.getDao(MenuDao.class);
        HeaderDto header = menuDao.getUserHeaderDetails(principal.getUserId());
        if (header == null) {
            throw new FitlyBussinessException(ErrorStatus.UNAUTHORIZED,
                    "USER_HEADER_NOT_FOUND_FOR_USER_" + principal.getUserId());
        }

        List<MenuDto> allMenus = menuDao.getAllActiveMenus();

        Set<String> permittedMenuIds = new HashSet<>();
        SessionContext ctx = Ctx.get();
        if (ctx.getRoleIds() != null) {
            for (java.util.UUID roleId : ctx.getRoleIds()) {
                permittedMenuIds.addAll(menuDao.getPermittedMenuIdsByRole(roleId));
            }
        }

        MenuService menuService = new MenuService();
        List<MenuDto> menuTree = menuService.buildAndPruneMenuTree(allMenus, permittedMenuIds);

        MainDisplayResponse response = new MainDisplayResponse();
        response.setHeader(header);
        response.setMenuList(menuTree);

        return response;
    }

    private UserPrincipal authenticate() throws Exception {
        SessionContext ctx = Ctx.get();
        if (ctx == null || ctx.getUser() == null) {
            throw new FitlyBussinessException(ErrorStatus.UNAUTHORIZED, "REQUEST_CONTEXT_MISSING");
        }

        return ctx.getUser();
    }
}
