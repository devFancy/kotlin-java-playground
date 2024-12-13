package dev.be.javaspringbootoauth.auth.service;


import dev.be.javaspringbootoauth.auth.domain.AuthToken;

public interface TokenCreator {
    AuthToken createAuthToken(final Long memberId);

    Long extractPayLoad(final String accessToken);

    AuthToken renewAuthToken(final String refreshToken);
}
