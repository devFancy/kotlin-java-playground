package dev.be.javaspringbootoauth.auth.repository;

import dev.be.javaspringbootoauth.auth.domain.entity.OAuthToken;
import dev.be.javaspringbootoauth.auth.exception.NotFoundOAuthTokenException;
import dev.be.javaspringbootoauth.member.domain.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface OAuthTokenRepository extends JpaRepository<OAuthToken, Long> {

    boolean existsByMemberId(final Long memberId);

    @Query("SELECT o "
            + "FROM OAuthToken o "
            + "WHERE o.member.id = :memberId")
    Optional<OAuthToken> findByMemberId(@Param("memberId") final Long memberId);

    default OAuthToken getByMemberId(final Long memberId) {
        return findByMemberId(memberId)
                .orElseThrow(() -> new NotFoundOAuthTokenException());
    }

    // 리프레시 토큰 제거
    void deleteAllByMemberId(final Long memberId);

    // 회원 탈퇴
    void deleteByMember(final Member member);
}
