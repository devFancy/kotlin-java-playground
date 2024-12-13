package dev.be.javaspringbootoauth.auth.dto.response;

public class AccessTokenResponse {

    private final Long id;

    private final String accessToken;


    public AccessTokenResponse(final Long id, final String accessToken) {
        this.id = id;
        this.accessToken = accessToken;
    }

    public Long getId() {
        return id;
    }

    public String getAccessToken() {
        return accessToken;
    }
}
