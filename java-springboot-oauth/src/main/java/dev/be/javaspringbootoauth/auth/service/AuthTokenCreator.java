package dev.be.javaspringbootoauth.auth.service;

import dev.be.javaspringbootoauth.auth.domain.AuthToken;
import dev.be.javaspringbootoauth.auth.repository.TokenRepository;
import org.springframework.stereotype.Component;

@Component
public class AuthTokenCreator implements TokenCreator {

    private final TokenProvider tokenProvider;

    private final TokenRepository tokenRepository;

    private final AuthTokenResponseHandler authTokenResponseHandler;

    public AuthTokenCreator(final TokenProvider tokenProvider,
                            final TokenRepository tokenRepository,
                            final AuthTokenResponseHandler authTokenResponseHandler) {
        this.tokenProvider = tokenProvider;
        this.tokenRepository = tokenRepository;
        this.authTokenResponseHandler = authTokenResponseHandler;
    }

    @Override
    public AuthToken createAuthToken(final Long memberId) {
        Long id = memberId;
        String accessToken = tokenProvider.createAccessToken(String.valueOf(memberId));
        String refreshToken = createRefreshToken(memberId);

        authTokenResponseHandler.setRefreshTokenCookie(refreshToken);
        return new AuthToken(id, accessToken);
    }

    private String createRefreshToken(final Long memberId) {
        if (tokenRepository.exist(memberId)) {
            return tokenRepository.getToken(memberId);
        }
        String refreshToken = tokenProvider.createRefreshToken(String.valueOf(memberId));
        return tokenRepository.save(memberId, refreshToken);
    }

    @Override
    public Long extractPayLoad(final String accessToken) {
        tokenProvider.validateToken(accessToken);
        return Long.valueOf(tokenProvider.getPayLoad(accessToken));
    }

    @Override
    public AuthToken renewAuthToken(final String refreshToken) {
        tokenProvider.validateToken(refreshToken);
        Long memberId = Long.valueOf(tokenProvider.getPayLoad(refreshToken));

        String accessTokenForRenew = tokenProvider.createAccessToken(String.valueOf(memberId));
        String refreshTokenForRenew = tokenRepository.getToken(memberId);

        authTokenResponseHandler.setRefreshTokenCookie(refreshTokenForRenew);

        AuthToken renewAuthAccessToken = new AuthToken(memberId, accessTokenForRenew);
        renewAuthAccessToken.validateHasSameRefreshToken(refreshTokenForRenew, refreshToken);
        return renewAuthAccessToken;
    }
}
