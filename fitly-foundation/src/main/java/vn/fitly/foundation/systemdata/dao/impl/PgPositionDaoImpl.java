/**
 * Project: Fitly Platform
 * Author:  fitly.zero
 * Date:    May 20, 2026
 * Time:    1:39:54 PM
 * * Copyright (c) 2026 fitly.zero. All rights reserved.
 * Licensed under the Apache License 2.0.
 */
package vn.fitly.foundation.systemdata.dao.impl;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import vn.fitly.foundation.cache.ADaoWithCache;
import vn.fitly.foundation.helper.DbHelper;
import vn.fitly.foundation.systemdata.dao.PositionDao;
import vn.fitly.foundation.systemdata.entity.Position;

/**
 * 
 */
public class PgPositionDaoImpl extends ADaoWithCache<UUID, Position> implements PositionDao {

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
                position.setPositionId(rs.getString("sys_position_id"));
                position.setCode(rs.getString("code"));
                position.setName(rs.getString("name"));
                position.setOrgId(rs.getString("sys_org_id"));

                rsList.add(position);
            }

            return rsList;
        }

    }

    @Override
    public boolean isValidUserRole(UUID userId, Position position) throws Exception {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    protected Map<UUID, Position> loadDataWithIds(Collection<UUID> ids) throws Exception {

//        StringBuilder sb = new StringBuilder();
//        sb.append("select p.*\n");
//        sb.append("from sys_position p \n");
//        sb.append("where up.sys_user_id = ?");
//
//        try (PreparedStatement ps = DbHelper.preparedStatement(sb.toString(), userId);
//                ResultSet rs = ps.executeQuery()) {
//
//            List<Position> rsList = new ArrayList<>();
//            while (rs.next()) {
//
//                Position position = new Position();
//                position.setPositionId(rs.getString("sys_position_id"));
//                position.setCode(rs.getString("code"));
//                position.setName(rs.getString("name"));
//                position.setOrgId(rs.getString("sys_org_id"));
//
//                rsList.add(position);
//            }
//
//            return rsList;
//        }

        return null;
    }

    @Override
    protected String getPrefix() {
        return "position";
    }

    @Override
    protected Class<Position> getClazz() {
        return Position.class;
    }

}
