package vn.fitly.iam.dto;

public class LogoutResponse {

    private boolean success;

    public LogoutResponse() {
    }

    public LogoutResponse(boolean success) {
        this.success = success;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}
