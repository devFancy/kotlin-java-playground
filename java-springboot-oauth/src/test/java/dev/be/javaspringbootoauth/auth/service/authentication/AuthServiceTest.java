package dev.be.javaspringbootoauth.auth.service.authentication;

import dev.be.javaspringbootoauth.auth.dto.request.TokenRenewalRequest;
import dev.be.javaspringbootoauth.auth.dto.response.AccessTokenResponse;
import dev.be.javaspringbootoauth.auth.event.MemberSavedEvent;
import dev.be.javaspringbootoauth.auth.exception.InvalidTokenException;
import dev.be.javaspringbootoauth.auth.repository.TokenRepository;
import dev.be.javaspringbootoauth.auth.service.AuthService;
import dev.be.javaspringbootoauth.common.OAuthFixtures;
import dev.be.javaspringbootoauth.global.config.JpaAuditingConfig;
import dev.be.javaspringbootoauth.member.domain.entity.Member;
import dev.be.javaspringbootoauth.member.repository.MemberRepository;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.event.ApplicationEvents;
import org.springframework.test.context.event.RecordApplicationEvents;

import java.util.List;

@Import(JpaAuditingConfig.class)
@ActiveProfiles("test")
@RecordApplicationEvents
@SpringBootTest
class AuthServiceTest {

    @Autowired
    private AuthService authService;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private TokenRepository tokenRepository;

    @Autowired
    private ApplicationEvents events;

    @AfterEach
    void tearDown() {
        tokenRepository.deleteAll();
    }

    @DisplayName("토큰을 생성을 하면 OAuth 서버에서 인증 후 토큰을 반환한다.")
    @Test
    void tokenGenerationAndAuthenticationFromOAuthServer() {
        // given
        AccessTokenResponse actual = authService.generateAccessAndRefreshToken(OAuthFixtures.MEMBER.getoAuthMember());

        // when & then
        Assertions.assertAll(() -> {
            assertThat(actual.getId()).isNotNull();
            assertThat(actual.getAccessToken()).isNotEmpty();
            assertThat(events.stream(MemberSavedEvent.class).count()).isEqualTo(1);
        });
    }

    @DisplayName("이미 가입된 회원에 대한 Authorization Code를 전달받으면 추가로 회원이 생성되지 않는다.")
    @Test
    void ifAuthorizationCodeExistsThenNotCreateMember() {
        // given
        // 액세스 토큰과 리프레시 토큰을 발급함과 동시에 회원 테이블에는 회원의 개인정보(이메일, 닉네임, 소셜 로그인 유형)과 OAuth 테이블에는 refreshToken(유효기간: 14일)이 저장됨
        authService.generateAccessAndRefreshToken(OAuthFixtures.MEMBER.getoAuthMember()); // 처음에 생성 -> accessToken, refreshToken

        // when
        // 이미 가입된 회원이 소셜 로그인 버튼을 클릭했을 경우에는 회원가입 과정이 생략된다.
        authService.generateAccessAndRefreshToken(OAuthFixtures.MEMBER.getoAuthMember()); // 두번째 이후부터는 accessToken, refreshToken 발급되지 X
        List<Member> actual = memberRepository.findAll();

        // then
        assertThat(actual).hasSize(1); // 액세스 토큰과 리프레시 토큰이 존재하는 한, 추가로 생성되지 않기에 1개만 남는다.
    }

    @DisplayName("이미 가입된 회원이고 저장된 AccessToken이 있으면, 저장된 AccessToken을 반환한다.")
    @Test
    void ExistingMemberAndRefreshTokenIsExistThenReturnRefreshToken() {
        // 이미 가입된 회원이 소셜 로그인 버튼을 클릭했을 경우엔 회원가입 과정이 생략되고, 곧바로 access token과 refreshtoken이 발급되어야 한다.
        // given
        AccessTokenResponse response = authService.generateAccessAndRefreshToken(OAuthFixtures.MEMBER.getoAuthMember());

        // when
        AccessTokenResponse actual = authService.generateAccessAndRefreshToken(OAuthFixtures.MEMBER.getoAuthMember());

        // then
        assertThat(actual.getAccessToken()).isEqualTo(response.getAccessToken());

    }

    @DisplayName("리프레시 토큰으로 새로운 액세스 토큰을 발급할 때, 리프레시 토큰이 유효하지 않으면 예외를 던진다.")
    @Test
    void ifRefreshTokenIsInvalidThenThrowException() {
        // given
        authService.generateAccessAndRefreshToken(OAuthFixtures.MEMBER.getoAuthMember());
        TokenRenewalRequest tokenRenewalRequest = new TokenRenewalRequest("DummyRefreshToken");

        // when & then
        assertThatThrownBy(() -> authService.generateAccessToken(tokenRenewalRequest))
                .isInstanceOf(InvalidTokenException.class);
    }
}