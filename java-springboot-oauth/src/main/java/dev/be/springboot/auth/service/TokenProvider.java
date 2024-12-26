package dev.be.springboot.auth.service;

public interface TokenProvider {

    String createAccessToken(final String payload);

    void validateToken(final String accessToken);

    String getPayLoad(final String accessToken);

    String createRefreshToken(final String payload);
}
