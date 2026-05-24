/**
 * Project: Fitly Platform
 * Author:  fitly.zero
 * Date:    May 23, 2026
 * Time:    1:15:16 PM
 * * Copyright (c) 2026 fitly.zero. All rights reserved.
 * Licensed under the Apache License 2.0.
 */
package vn.fitly.foundation.processor;

/**
 * 
 */
public interface IProcessor<RQ, RP> {
    
    public RP process();

    
}
