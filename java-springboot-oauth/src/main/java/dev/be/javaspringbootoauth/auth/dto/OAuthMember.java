package dev.be.javaspringbootoauth.auth.dto;

import dev.be.javaspringbootoauth.member.domain.SocialType;
import dev.be.javaspringbootoauth.member.domain.entity.Member;
import lombok.Getter;

@Getter
public class OAuthMember {

    private final String email;

    private final String nickname;

    private SocialType socialType;

    private final String refreshToken;

    private final boolean deleted = false;


    public OAuthMember(final String email, final String nickname, final String refreshToken) {
        this.email = email;
        this.nickname = nickname;
        this.refreshToken = refreshToken;
    }

    public OAuthMember(final String email, final String nickname, final SocialType socialType, final String refreshToken) {
        this.email = email;
        this.nickname = nickname;
        this.socialType = socialType;
        this.refreshToken = refreshToken;
    }

    public Member toMember() {
        // `scheduledDeletionTime`, `unregisterReason`, `feedback`을 null로 설정
        return new Member(email, nickname, socialType);
    }
}
