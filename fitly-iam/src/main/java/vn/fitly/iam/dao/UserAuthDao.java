package vn.fitly.iam.dao;

import java.util.List;
import java.util.UUID;

import vn.fitly.iam.model.Position;
import vn.fitly.iam.model.TenantProfile;
import vn.fitly.iam.model.User;

public interface UserAuthDao {

    TenantProfile getTenantById(UUID tenantId) throws Exception;

    TenantProfile getTenantByCode(String tenantCode) throws Exception;

    User getActiveUserByUsername(String username) throws Exception;

    Position getDefaultPosition(UUID userId) throws Exception;

    List<UUID> getRoleIdsByPosition(UUID positionId) throws Exception;
}
