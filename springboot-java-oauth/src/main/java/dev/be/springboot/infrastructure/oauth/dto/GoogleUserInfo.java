package dev.be.springboot.infrastructure.oauth.dto;

public class GoogleUserInfo {

    private String email;
    private String name;

    private GoogleUserInfo() {
    }

    public GoogleUserInfo(final String email, final String name) {
        this.email = email;
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }
}
