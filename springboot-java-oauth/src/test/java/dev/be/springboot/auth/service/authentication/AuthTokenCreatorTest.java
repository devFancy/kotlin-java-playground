package dev.be.springboot.auth.service.authentication;

import dev.be.springboot.config.ExternalApiConfig;
import dev.be.springboot.auth.domain.AuthToken;
import dev.be.springboot.auth.service.TokenCreator;
import dev.be.springboot.global.config.JpaAuditingConfig;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

@Import(JpaAuditingConfig.class)
@ActiveProfiles("test")
@SpringBootTest(classes = ExternalApiConfig.class)
class AuthTokenCreatorTest {

    @Autowired
    private TokenCreator tokenCreator;

    @DisplayName("액세스 토큰과 리프레시 토큰을 발급한다.")
    @Test
    void createAccessAndRefreshToken() {
        // given
        Long memberId = 1L;

        // when
        AuthToken authToken = tokenCreator.createAuthToken(memberId);

        // then
        assertThat(authToken.getId()).isNotNull();
        assertThat(authToken.getAccessToken()).isNotEmpty();
    }

    @DisplayName("액세스 토큰에서 페이로드 값을 추출한다.")
    @Test
    void extractPayloadFromAccessTokenAndRefreshToken() {
        // given
        Long memberId = 1L;
        AuthToken authToken = tokenCreator.createAuthToken(memberId);

        // when
        Long actualFromAccessToken = tokenCreator.extractPayLoad(authToken.getAccessToken());

        // then
        assertThat(actualFromAccessToken).isEqualTo(memberId);
    }
}