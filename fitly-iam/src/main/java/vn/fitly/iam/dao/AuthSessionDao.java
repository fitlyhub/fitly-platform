package vn.fitly.iam.dao;

import vn.fitly.iam.model.AuthSession;

public interface AuthSessionDao {

    void create(AuthSession session) throws Exception;

    AuthSession getBySessionTokenHash(String sessionTokenHash) throws Exception;

    void revokeBySessionTokenHash(String sessionTokenHash) throws Exception;
}
