package vn.fitly.foundation.security;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Base64;

import vn.fitly.common.exception.ErrorStatus;
import vn.fitly.common.exception.FitlyRuntimeException;
import vn.fitly.common.language.DefaultSystemMessage;
import vn.fitly.common.utils.StringUtils;

public final class SessionHash {

    private SessionHash() {
    }

    public static String hash(String sessionSecret) {
        if (StringUtils.isBlank(sessionSecret)) {
            return null;
        }

        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(sessionSecret.getBytes(StandardCharsets.UTF_8));
            return Base64.getUrlEncoder().withoutPadding().encodeToString(hash);
        } catch (Exception e) {
            throw new FitlyRuntimeException(ErrorStatus.INTERNAL_ERROR, DefaultSystemMessage.INTERNAL_ERROR.name(), e);
        }
    }
}
