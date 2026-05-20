/**
 * Project: Fitly Platform
 * Author:  fitly.zero
 * Date:    May 20, 2026
 * Time:    1:39:54 PM
 * * Copyright (c) 2026 fitly.zero. All rights reserved.
 * Licensed under the Apache License 2.0.
 */
package vn.fitly.iam.dao.impl.pg;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import vn.fitly.foundation.helper.DbHelper;
import vn.fitly.iam.dao.RoleDao;
import vn.fitly.iam.model.Position;

/**
 * 
 */
public class PgRoleDaoImpl implements RoleDao {

    @Override
    public List<Position> getUserPositions(UUID userId) throws Exception {

        StringBuilder sb = new StringBuilder();
        sb.append("select p.*\n");
        sb.append("from sys_user_position up\n");
        sb.append("join sys_position p on up.sys_position_id = p.sys_position_id\n");
        sb.append("where up.sys_user_id = ?");

        try (PreparedStatement ps = DbHelper.preparedStatement(sb.toString(), userId);
                ResultSet rs = ps.executeQuery()) {

            List<Position> rsList = new ArrayList<>();
            while (rs.next()) {

                Position position = new Position();
                position.setCode(rs.getString("code"));
                position.setName(rs.getString("name"));
                position.setOrgId(rs.getString("sys_org_id"));

                rsList.add(position);
            }

            return rsList;
        }

    }

}
