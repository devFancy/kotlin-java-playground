package dev.be.springboot.auth.dto.request;

public class RefreshTokenRequest {

    private String email;

    public RefreshTokenRequest(final String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }
}
