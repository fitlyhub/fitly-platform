/**
 * Project: Fitly Platform
 * Author:  fitly.zero
 * Date:    May 21, 2026
 * Time:    11:52:31 AM
 * * Copyright (c) 2026 fitly.zero. All rights reserved.
 * Licensed under the Apache License 2.0.
 */
package vn.fitly.common.utils;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;

/**
 * 
 */
public class DateTimeUtils {

    public static LocalDateTime now(ZoneId zoneId) {
        return LocalDateTime.now(zoneId);
    }
    
    public static Instant now() {
        return Instant.now();
    }
    
    public static LocalDateTime nowUtc() {
        return LocalDateTime.now(ZoneOffset.UTC);
    }
}