package vn.fitly.generated.context;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "fitly.session")
public class FitlySessionConfig {

    private String cookieName = "FITLY_SESSION";

    private String loginChallengeCookieName = "FITLY_LOGIN_CHALLENGE";

    private boolean cookieSecure = true;

    private boolean cookieHttpOnly = true;

    private String cookieSameSite = "Lax";

    private String cookiePath = "/";

    private String loginChallengeCookiePath = "/api/v1/auth";

    private String cookieDomain;

    private long expireMinutes = 600;

    private long loginChallengeExpireMinutes = 5;

    public String getCookieName() {
        return cookieName;
    }

    public void setCookieName(String cookieName) {
        this.cookieName = cookieName;
    }

    public String getLoginChallengeCookieName() {
        return loginChallengeCookieName;
    }

    public void setLoginChallengeCookieName(String loginChallengeCookieName) {
        this.loginChallengeCookieName = loginChallengeCookieName;
    }

    public boolean isCookieSecure() {
        return cookieSecure;
    }

    public void setCookieSecure(boolean cookieSecure) {
        this.cookieSecure = cookieSecure;
    }

    public boolean isCookieHttpOnly() {
        return cookieHttpOnly;
    }

    public void setCookieHttpOnly(boolean cookieHttpOnly) {
        this.cookieHttpOnly = cookieHttpOnly;
    }

    public String getCookieSameSite() {
        return cookieSameSite;
    }

    public void setCookieSameSite(String cookieSameSite) {
        this.cookieSameSite = cookieSameSite;
    }

    public String getCookiePath() {
        return cookiePath;
    }

    public void setCookiePath(String cookiePath) {
        this.cookiePath = cookiePath;
    }

    public String getLoginChallengeCookiePath() {
        return loginChallengeCookiePath;
    }

    public void setLoginChallengeCookiePath(String loginChallengeCookiePath) {
        this.loginChallengeCookiePath = loginChallengeCookiePath;
    }

    public String getCookieDomain() {
        return cookieDomain;
    }

    public void setCookieDomain(String cookieDomain) {
        this.cookieDomain = cookieDomain;
    }

    public long getExpireMinutes() {
        return expireMinutes;
    }

    public void setExpireMinutes(long expireMinutes) {
        this.expireMinutes = expireMinutes;
    }

    public long getLoginChallengeExpireMinutes() {
        return loginChallengeExpireMinutes;
    }

    public void setLoginChallengeExpireMinutes(long loginChallengeExpireMinutes) {
        this.loginChallengeExpireMinutes = loginChallengeExpireMinutes;
    }

}
