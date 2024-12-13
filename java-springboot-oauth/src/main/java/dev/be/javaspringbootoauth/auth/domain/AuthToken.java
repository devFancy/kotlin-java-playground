package dev.be.javaspringbootoauth.auth.domain;


import dev.be.javaspringbootoauth.auth.exception.NotFoundTokenException;
import dev.be.javaspringbootoauth.global.error.ErrorCode;

public class AuthToken {

    private final Long id;

    private final String accessToken;

    public AuthToken(final Long id, final String accessToken) {
        this.id = id;
        this.accessToken = accessToken;
    }

    public Long getId() {
        return id;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void validateHasSameRefreshToken(final String refreshTokenForRenew, final String otherRefreshToken) {
        if (!refreshTokenForRenew.equals(otherRefreshToken)) {
            throw new NotFoundTokenException(ErrorCode.NOT_FOUND_TOKEN, "The refresh token does not belong to the member."); // 회원의 리프레시 토큰이 아닙니다
        }
    }
}
