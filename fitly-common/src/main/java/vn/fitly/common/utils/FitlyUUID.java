/**
 * Project: Fitly Platform Author:  fitly.zero Date:    May 22, 2026 Time:    2:17:33 PM * Copyright (c) 2026
 * fitly.zero. All rights reserved. Licensed under the Apache License 2.0.
 */
package vn.fitly.common.utils;

import com.fasterxml.uuid.Generators;
import com.fasterxml.uuid.impl.TimeBasedEpochGenerator;

import java.util.UUID;

/**
 *
 */
public class FitlyUUID {

    private static final TimeBasedEpochGenerator UUID_V7_GENERATOR = Generators.timeBasedEpochGenerator();

    private FitlyUUID() {
    }

    public static UUID newId() {
        return UUID_V7_GENERATOR.generate();
    }

}
