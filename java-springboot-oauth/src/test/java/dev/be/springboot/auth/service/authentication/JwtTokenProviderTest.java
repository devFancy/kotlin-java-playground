package dev.be.springboot.auth.service.authentication;

import dev.be.springboot.auth.exception.InvalidTokenException;
import dev.be.springboot.auth.service.JwtTokenProvider;
import dev.be.springboot.auth.service.TokenProvider;
import org.apache.commons.lang3.StringUtils;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class JwtTokenProviderTest {

    private static final String JWT_SECRET_KEY = StringUtils.repeat("A",32); // Secret Key는 최소 32 바이트 이상이어야 함
    private static final int JWT_ACCESS_TOKEN_EXPIRE_LENGTH = 3600;
    private static final int JWT_REFRESH_TOKEN_EXPIRE_LENGTH = 3600;
    private static final String PAYLOAD = "payload";

    private final JwtTokenProvider jwtTokenProvider = new JwtTokenProvider(
            JWT_SECRET_KEY, JWT_ACCESS_TOKEN_EXPIRE_LENGTH, JWT_REFRESH_TOKEN_EXPIRE_LENGTH);

    @DisplayName("액세스 토큰을 생성한다.")
    @Test
    void createAccessToken() {
        // arrange & act
        String actual = jwtTokenProvider.createAccessToken(PAYLOAD);

        // assert
        assertThat(actual.split("\\.")).hasSize(3);
    }

    @DisplayName("리프레시 토큰을 생성한다.")
    @Test
    void createRefreshToken() {
        // arrange
        String actual = jwtTokenProvider.createRefreshToken(PAYLOAD);

        // act & assert
        assertThat(actual.split("\\.")).hasSize(3);
    }

    @DisplayName("만약 액세스 토큰을 검증하여 만료된 경우 예외를 던진다.")
    @Test
    void ifAccessTokenExpiredThenInvalidTokenException() {
        // arrange
        TokenProvider expiredJwtTokenProvider = new JwtTokenProvider(JWT_SECRET_KEY, 0, 0);
        String expireToken = expiredJwtTokenProvider.createAccessToken(PAYLOAD);

        // act & assert
        assertThatThrownBy(() -> jwtTokenProvider.validateToken(expireToken))
                .isInstanceOf(InvalidTokenException.class);
    }

    @DisplayName("액세스 토큰을 검증하여 만료된 경우 예외를 던진다.")
    @Test
    void ifAccessTokenIsExpiredThenInvalidTokenException() {
        // arrange
        TokenProvider expiredJwtTokenProvider = new JwtTokenProvider(JWT_SECRET_KEY, 0, 3600);
        String expireAccessToken = expiredJwtTokenProvider.createAccessToken(PAYLOAD);

        // act & assert
        assertThatThrownBy(() -> jwtTokenProvider.validateToken(expireAccessToken))
                .isInstanceOf(InvalidTokenException.class);
    }

    @DisplayName("리프레시 토큰을 검증하여 만료된 경우 예외를 던진다.")
    @Test
    void ifRefreshTokenIsExpiredThenInvalidTokenException() {
        // arrange
        TokenProvider expiredJwtTokenProvider = new JwtTokenProvider(JWT_SECRET_KEY, 3600, 0);
        String expireRefreshToken = expiredJwtTokenProvider.createRefreshToken(PAYLOAD);

        // act & assert
        assertThatThrownBy(() -> jwtTokenProvider.validateToken(expireRefreshToken))
                .isInstanceOf(InvalidTokenException.class);
    }

    @DisplayName("토큰을 검증하여 유효하지 않으면 예외를 던진다.")
    @Test
    void ifTokenIsInvalidThenInvalidTokenException() {
        // arrange
        String invalidToken = "invalidToken";

        // act & assert
        assertThatThrownBy(() -> jwtTokenProvider.validateToken(invalidToken))
                .isInstanceOf(InvalidTokenException.class);
    }
}