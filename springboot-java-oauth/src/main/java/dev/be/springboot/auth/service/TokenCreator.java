package dev.be.springboot.auth.service;


import dev.be.springboot.auth.domain.AuthToken;

public interface TokenCreator {
    AuthToken createAuthToken(final Long memberId);

    Long extractPayLoad(final String accessToken);

    AuthToken renewAuthToken(final String refreshToken);
}
