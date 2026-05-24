package vn.fitly.foundation.security;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Base64;

import vn.fitly.common.exception.ErrorStatus;
import vn.fitly.common.exception.FitlyRuntimeException;
import vn.fitly.common.language.DefaultSystemMessage;
import vn.fitly.common.utils.StringUtils;

public final class SessionHelper {

    private static final int SECRET_BYTES = 32;

    private static final SecureRandom SECURE_RANDOM = new SecureRandom();

    private SessionHelper() {
    }

    public static String generateSession() {
        byte[] bytes = new byte[SECRET_BYTES];
        SECURE_RANDOM.nextBytes(bytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
    }
    
    public static String sha256(String sessionSecret) {

        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(sessionSecret.getBytes(StandardCharsets.UTF_8));
            return Base64.getUrlEncoder().withoutPadding().encodeToString(hash);
        } catch (Exception e) {
            throw new FitlyRuntimeException(ErrorStatus.INTERNAL_ERROR, DefaultSystemMessage.INTERNAL_ERROR.name(), e);
        }
    }
}
