/**
 * Project: Fitly Platform
 * Author:  fitly.zero
 * Date:    May 21, 2026
 * Time:    2:00:00 PM
 * Copyright (c) 2026 fitly.zero. All rights reserved.
 * Licensed under the Apache License 2.0.
 */
package vn.fitly.studio.dao;

import java.util.List;
import java.util.Set;
import java.util.UUID;

import vn.fitly.foundation.context.UserPrincipal;
import vn.fitly.studio.dto.HeaderDto;
import vn.fitly.studio.dto.MenuDto;

/**
 * DAO for main display menu and header metadata.
 */
public interface MenuDao {

    UserPrincipal getUserPrincipal(UUID userId) throws Exception;

    HeaderDto getUserHeaderDetails(UUID userId) throws Exception;

    List<MenuDto> getAllActiveMenus() throws Exception;

    Set<String> getPermittedMenuIdsByRole(UUID roleId) throws Exception;
}
