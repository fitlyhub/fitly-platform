package vn.fitly.iam.processor.v1;

import vn.fitly.common.exception.ErrorStatus;
import vn.fitly.common.exception.FitlyBussinessException;
import vn.fitly.common.utils.StringUtils;
import vn.fitly.foundation.dao.DaoFactory;
import vn.fitly.foundation.processor.BaseProcessor;
import vn.fitly.foundation.security.SessionCache;
import vn.fitly.iam.dao.AuthSessionDao;
import vn.fitly.iam.dto.LogoutResponse;

public class Logout extends BaseProcessor<String, LogoutResponse> {

    public Logout(String sessionTokenHash) {
        super(sessionTokenHash);
    }

    @Override
    protected void validate() throws Exception {
        if (StringUtils.isBlank(request)) {
            throw new FitlyBussinessException(ErrorStatus.UNAUTHORIZED, "SESSION_REQUIRED");
        }
    }

    @Override
    protected LogoutResponse doProcess() throws Exception {
        AuthSessionDao sessionDao = DaoFactory.getDao(AuthSessionDao.class);
        sessionDao.revokeBySessionTokenHash(request);
        SessionCache.remove(request);
        return new LogoutResponse(true);
    }
}
