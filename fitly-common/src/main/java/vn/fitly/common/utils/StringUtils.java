package vn.fitly.common.utils;

/**
 * Project: Fitly Platform Author: fitly.zero Date: 29/4/26 Time: 11:39 *
 * Copyright (c) 2026 fitly.zero. All rights reserved. Licensed under the Apache
 * License 2.0.
 */
public class StringUtils {

    public static String trimToNull(String str) {
        if (str == null) {
            return null;
        }

        str = str.trim();
        if (str.isEmpty()) {
            return null;
        }

        return str;
    }

    public static boolean isBlank(String str) {
        if (str == null) {
            return true;
        }

        str = str.trim();
        if (str.isEmpty()) {
            return true;
        }

        return false;
    }

    public static String merge(String... args) {

        if (args == null || args.length < 1) {
            return null;
        }

        StringBuilder sb = new StringBuilder();
        for (String str : args) {

            if (str == null) {
                continue;
            }

            sb.append(str);
        }

        return sb.toString();

    }

    public static String mergeKey(String... args) {

        if (args == null || args.length < 1) {
            return null;
        }

        StringBuilder sb = new StringBuilder();
        for (String str : args) {

            if (str == null) {
                continue;
            }

            sb.append(str);
            sb.append(":");
        }

        return sb.toString();

    }

}
