package dev.be.springboot.auth.domain;

import dev.be.springboot.auth.exception.NotFoundTokenException;
import dev.be.springboot.auth.repository.InMemoryAuthTokenRepository;
import dev.be.springboot.auth.repository.TokenRepository;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class InMemoryAuthTokenRepositoryTest {

    private final TokenRepository tokenRepository = new InMemoryAuthTokenRepository();

    @BeforeEach
    void setUp() {
        tokenRepository.deleteAll();
    }

    @DisplayName("토큰을 저장한다.")
    @Test
    void saveToken() {
        // given
        Long dummyMemberId = 1L;
        String dummyRefreshToken = "dummy token";

        // when
        tokenRepository.save(dummyMemberId, dummyRefreshToken);

        // then
        assertThat(tokenRepository.getToken(dummyMemberId)).isEqualTo(dummyRefreshToken);
    }

    @DisplayName("MemberId에 해당하는 토큰이 있으면 true를 반환한다.")
    @Test
    void existsTokenForMemberId() {
        // given
        Long dummyMemberId = 1L;
        String dummyRefreshToken = "dummy token";
        tokenRepository.save(dummyMemberId, dummyRefreshToken);

        // when
        boolean actual = tokenRepository.exist(dummyMemberId);

        // then
        assertThat(actual).isTrue();
    }

    @DisplayName("MemberId에 해당하는 토큰이 없으면 false를 반환한다.")
    @Test
    void doesNotExistTokenForMemberId() {
        // given
        Long dummyMemberId = 1L;

        // when
        boolean actual = tokenRepository.exist(dummyMemberId);

        // then
        assertThat(actual).isFalse();
    }

    @DisplayName("MemberId에 해당하는 토큰을 가져온다.")
    @Test
    void retrieveTokenForMemberId() {
        // given
        Long dummyMemberId = 1L;
        String dummyRefreshToken = "dummy token";
        tokenRepository.save(dummyMemberId, dummyRefreshToken);

        // when
        String actual = tokenRepository.getToken(dummyMemberId);

        // then
        assertThat(actual).isEqualTo(dummyRefreshToken);
    }

    @DisplayName("MemberId에 해당하는 토큰이 없으면 예외를 발생한다.")
    @Test
    void IfTokenNotExistForMemberIdThenThrowException() {
        // given
        Long dummyMemberId = 1L;

        // when & then
        assertThatThrownBy(() -> tokenRepository.getToken(dummyMemberId))
                .isInstanceOf(NotFoundTokenException.class);
    }
}