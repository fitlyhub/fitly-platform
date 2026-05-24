package vn.fitly.common.utils;

import java.math.BigDecimal;

/**
 * Project: Fitly Platform Author:  fitly.zero Date:    24/5/26 Time:    13:54 * Copyright (c) 2026 fitlyzero. All
 * rights reserved. Licensed under the Apache License 2.0.
 */
public class NumberUtils {

    public static BigDecimal parseNumber(String value) {

        if (StringUtils.isBlank(value)) {
            return null;
        }

        try {
            return new BigDecimal(value);
        } catch (Exception e) {
            return null;
        }

    }

    public static BigDecimal toZero(BigDecimal value) {
        if (value == null) {
            return BigDecimal.ZERO;
        }

        return value;
    }
}
