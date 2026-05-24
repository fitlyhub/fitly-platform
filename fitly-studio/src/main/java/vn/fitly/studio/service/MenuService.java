/**
 * Project: Fitly Platform
 * Author:  fitly.zero
 * Date:    May 21, 2026
 * Time:    2:00:00 PM
 * Copyright (c) 2026 fitly.zero. All rights reserved.
 * Licensed under the Apache License 2.0.
 */
package vn.fitly.studio.service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import vn.fitly.studio.dto.MenuDto;

/**
 * Builds a permitted tree from flat menu data.
 */
public class MenuService {

    public List<MenuDto> buildAndPruneMenuTree(List<MenuDto> menus, Set<String> permittedMenuIds) {
        if (menus == null || menus.isEmpty()) {
            return new ArrayList<>();
        }

        Map<String, MenuDto> menuById = new HashMap<>();
        List<MenuDto> roots = new ArrayList<>();

        for (MenuDto menu : menus) {
            if (menu == null || menu.getMenuId() == null) {
                continue;
            }

            menu.setChildren(new ArrayList<>());
            menuById.put(menu.getMenuId(), menu);
        }

        for (MenuDto menu : menuById.values()) {
            MenuDto parent = menuById.get(menu.getParentMenuId());
            if (parent == null) {
                roots.add(menu);
                continue;
            }

            parent.getChildren().add(menu);
        }

        sortMenus(roots);

        return pruneMenus(roots, permittedMenuIds);
    }

    private List<MenuDto> pruneMenus(List<MenuDto> menus, Set<String> permittedMenuIds) {
        List<MenuDto> visibleMenus = new ArrayList<>();
        if (menus == null || menus.isEmpty()) {
            return visibleMenus;
        }

        for (MenuDto menu : menus) {
            List<MenuDto> visibleChildren = pruneMenus(menu.getChildren(), permittedMenuIds);
            boolean isPermitted = permittedMenuIds != null && permittedMenuIds.contains(menu.getMenuId());

            if (!isPermitted && visibleChildren.isEmpty()) {
                continue;
            }

            menu.setChildren(visibleChildren);
            visibleMenus.add(menu);
        }

        sortMenus(visibleMenus);

        return visibleMenus;
    }

    private void sortMenus(List<MenuDto> menus) {
        if (menus == null || menus.isEmpty()) {
            return;
        }

        menus.sort(Comparator.comparingInt(MenuDto::getSeqNo)
                .thenComparing(MenuDto::getName, Comparator.nullsLast(String::compareToIgnoreCase)));

        for (MenuDto menu : menus) {
            sortMenus(menu.getChildren());
        }
    }
}
