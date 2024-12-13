package dev.be.javaspringbootoauth.infrastructure.oauth.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class GoogleTokenResponse {

    private String accessToken;
    private String refreshToken;
    private String idToken;
    private String expiresIn;
    private String tokenType;
    private String scope;

    private GoogleTokenResponse() {
    }

    public GoogleTokenResponse(final String accessToken,
                               final String refreshToken,
                               final String idToken,
                               final String expiresIn,
                               final String tokenType,
                               final String scope) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.idToken = idToken;
        this.expiresIn = expiresIn;
        this.tokenType = tokenType;
        this.scope = scope;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public String getIdToken() {
        return idToken;
    }

    public String getExpiresIn() {
        return expiresIn;
    }

    public String getTokenType() {
        return tokenType;
    }

    public String getScope() {
        return scope;
    }
}
