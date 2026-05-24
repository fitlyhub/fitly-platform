package vn.fitly.iam.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import vn.fitly.foundation.request.ARequest;

public class LoginRequest extends ARequest {

    @JsonProperty("tenant_id")
    private String tenantId;

    @JsonProperty("tenant_code")
    private String tenantCode;

    private String username;

    private String password;

    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public String getTenantCode() {
        return tenantCode;
    }

    public void setTenantCode(String tenantCode) {
        this.tenantCode = tenantCode;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
