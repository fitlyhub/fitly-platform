/**
 * Project: Fitly Platform
 * Author:  fitly.zero
 * Date:    May 19, 2026
 * Time:    3:18:01 PM
 * * Copyright (c) 2026 fitly.zero. All rights reserved.
 * Licensed under the Apache License 2.0.
 */
package vn.fitly.iam.dao;

import java.util.List;

import vn.fitly.iam.model.Position;

/**
 * 
 */
public interface RoleDao {
    
    List<Position> getUserPositions(String userId);
    
    

}
