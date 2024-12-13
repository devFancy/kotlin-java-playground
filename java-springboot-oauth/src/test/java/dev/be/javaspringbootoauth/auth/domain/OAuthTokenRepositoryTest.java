package dev.be.javaspringbootoauth.auth.domain;

import dev.be.javaspringbootoauth.auth.domain.entity.OAuthToken;
import dev.be.javaspringbootoauth.auth.repository.OAuthTokenRepository;
import dev.be.javaspringbootoauth.common.MemberFixtures;
import dev.be.javaspringbootoauth.common.OAuthTokenFixtures;
import dev.be.javaspringbootoauth.global.config.JpaAuditingConfig;
import dev.be.javaspringbootoauth.member.domain.entity.Member;
import dev.be.javaspringbootoauth.member.repository.MemberRepository;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

@Import(JpaAuditingConfig.class)
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE) // 기본 임베디드 DB 사용을 비활성화
@DataJpaTest
class OAuthTokenRepositoryTest {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private OAuthTokenRepository oAuthTokenRepository;

    @DisplayName("member id의 OAuthToken이 존재할 경우 true를 반환한다.")
    @Test
    void IfExistsOAuthTokenForMemberIdThenTrue() {
        // given
        Member 팬시 = memberRepository.save(MemberFixtures.팬시());
        oAuthTokenRepository.save(new OAuthToken(팬시, OAuthTokenFixtures.REFRESH_TOKEN));

        // when
        boolean actual = oAuthTokenRepository.existsByMemberId(팬시.getId());

        // then
        assertThat(actual).isTrue();
    }

    @DisplayName("member id의 OAuthToken이 존재하지 않을 경우 false를 반환한다.")
    @Test
    void IfDoesNotExistOAuthTokenForMemberIdThenFalse() {
        // given & when
        boolean actual = oAuthTokenRepository.existsByMemberId(0L);

        // then
        assertThat(actual).isFalse();
    }

    @DisplayName("member id의 OAuthToken이 존재할 경우 Optional은 비어있지 않다.")
    @Test
    void notEmptyOptionalForOAuthTokenForMemberId() {
        // given
        Member 팬시 = memberRepository.save(MemberFixtures.팬시());
        oAuthTokenRepository.save(new OAuthToken(팬시, OAuthTokenFixtures.REFRESH_TOKEN));

        // when
        Optional<OAuthToken> actual = oAuthTokenRepository.findByMemberId(팬시.getId());

        // then
        assertThat(actual).isNotEmpty();
    }

    @DisplayName("member id의 OAuthToken이 존재하지 않을 경우 비어있다.")
    @Test
    void emptyOptionalForOAuthTokenForMemberId() {
        // given & when
        Optional<OAuthToken> actual = oAuthTokenRepository.findByMemberId(0L);

        // then
        assertThat(actual).isEmpty();
    }
}