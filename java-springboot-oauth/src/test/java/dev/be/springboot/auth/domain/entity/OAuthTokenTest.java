package dev.be.springboot.auth.domain.entity;

import dev.be.springboot.common.MemberFixtures;
import dev.be.springboot.member.domain.entity.Member;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class OAuthTokenTest {

    @DisplayName("OAuth token을 생성한다.")
    @Test
    void createOAuthToken() {
        // given
        Member 팬시 = MemberFixtures.팬시();
        String refreshToken = "aaaaaa.bbbbbb.cccccc.refreshToken";

        // when & then
        assertDoesNotThrow(() -> new OAuthToken(팬시, refreshToken));
    }

    @DisplayName("refresh token을 교체한다.")
    @Test
    void changeRefreshToken() {
        // given
        Member 팬시 = MemberFixtures.팬시();
        String refreshToken = "aaaaaa.bbbbbb.cccccc.refreshToken";
        OAuthToken oAuthToken = new OAuthToken(팬시, refreshToken);

        String updateRefreshToken = "cccccc.bbbbbb.aaaaaa.refreshToken";

        // when
        oAuthToken.change(updateRefreshToken);

        // then
        assertThat(oAuthToken.getRefreshToken()).isEqualTo(updateRefreshToken);
    }
}