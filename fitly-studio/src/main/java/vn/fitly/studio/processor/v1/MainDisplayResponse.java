/**
 * Project: Fitly Platform
 * Author:  fitly.zero
 * Date:    May 21, 2026
 * Time:    2:00:00 PM
 * Copyright (c) 2026 fitly.zero. All rights reserved.
 * Licensed under the Apache License 2.0.
 */
package vn.fitly.studio.processor.v1;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import vn.fitly.studio.dto.HeaderDto;
import vn.fitly.studio.dto.MenuDto;

/**
 * Response for the main display bootstrap API.
 */
public class MainDisplayResponse {

    @JsonProperty("header")
    private HeaderDto header;

    @JsonProperty("menu_list")
    private List<MenuDto> menuList = new ArrayList<>();

    public HeaderDto getHeader() {
        return header;
    }

    public void setHeader(HeaderDto header) {
        this.header = header;
    }

    public List<MenuDto> getMenuList() {
        return menuList;
    }

    public void setMenuList(List<MenuDto> menuList) {
        this.menuList = menuList;
    }
}
