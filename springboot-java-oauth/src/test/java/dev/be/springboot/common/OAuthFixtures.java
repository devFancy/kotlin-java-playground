package dev.be.springboot.common;

import dev.be.springboot.auth.dto.OAuthMember;
import dev.be.springboot.member.domain.SocialType;

import java.util.Arrays;
import java.util.NoSuchElementException;


public enum OAuthFixtures {

    팬시("팬시", 팬시()),
    MEMBER("member authorization code", MEMBER());

    private String code;
    private OAuthMember oAuthMember;

    OAuthFixtures(final String code, final OAuthMember oAuthMember) {
        this.code = code;
        this.oAuthMember = oAuthMember;
    }

    public static OAuthMember parseOAuthMember(final String code) {
        OAuthFixtures oAuthFixtures = Arrays.stream(values())
                .filter(value -> value.code.equals(code))
                .findFirst()
                .orElseThrow(NoSuchElementException::new);
        return oAuthFixtures.oAuthMember;
    }

    private static OAuthMember 팬시() {
        String 팬시_이메일 = "parang@email.com";
        String 팬시_이름 = "팬시";
        SocialType 팬시_소셜로그인_유형_구글 = SocialType.GOOGLE;
        String 팬시_REFRESH_TOKEN = "aaaaaaaaaa.bbbbbbbbbb.cccccccccc";
        return new OAuthMember(팬시_이메일, 팬시_이름, 팬시_소셜로그인_유형_구글, 팬시_REFRESH_TOKEN);
    }

    private static OAuthMember MEMBER() {
        String MEMBER_이메일 = "member@email.com";
        String MEMBER_이름 = "member";
        SocialType MEMBER_소셜로그인_유형_구글 = SocialType.GOOGLE;
        String MEMBER_REFRESH_TOKEN = "aaaaaaaaaa.bbbbbbbbbb.cccccccccc";
        return new OAuthMember(MEMBER_이메일, MEMBER_이름, MEMBER_소셜로그인_유형_구글, MEMBER_REFRESH_TOKEN);
    }
    public String getCode() {
        return code;
    }

    public OAuthMember getoAuthMember() {
        return oAuthMember;
    }
}
