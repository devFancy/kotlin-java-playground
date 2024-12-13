package dev.be.javaspringbootoauth.common;


import dev.be.javaspringbootoauth.auth.dto.request.TokenRenewalRequest;
import dev.be.javaspringbootoauth.auth.dto.request.TokenRequest;
import dev.be.javaspringbootoauth.auth.dto.response.AccessTokenResponse;

public class AuthFixtures {

    public static final String GOOGLE_PROVIDER = "google";
    public static final String KAKAO_PROVIDER = "kakao";
    public static final String OAUTH_PROVIDER = "oauthProvider";

    public static final String STUB_MEMBER_인증_코드 = "member authorization code";
    public static final String STUB_MEMBER_REFRESH_인증_코드 = "member refresh authorization code";

    public static final String 더미_엑세스_토큰 = "aaaaa.bbbbb.ccccc";
    public static final String 더미_리프레시_토큰 = "ccccc.bbbbb.aaaaa";

    public static final String OAuth_구글_로그인_링크 = "https://accounts.google.com/o/oauth2/v2/auth";
    public static final String OAuth_카카오_로그인_링크 = "https://kauth.kakao.com/oauth/authorize";

    public static final String MEMBER_이메일 = "member@email.com";

    public static final String 더미_시크릿_키 = "dummysecretkeyijuhnbmsowishcxbzcsdjsajdabwcksjadaksdhabdsadasjkdb";

    public static final String STUB_OAUTH_ACCESS_TOKEN = "aaaaaaaaaa.bbbbbbbbbb.cccccccccc";

    public static TokenRequest MEMBER_인증_코드_토큰_요청() {
        return new TokenRequest(STUB_MEMBER_인증_코드, "http://localhost:3000/google-callback");
    }

    public static AccessTokenResponse MEMBER_인증_코드_토큰_응답() {
        Long stubMemberId = 1L; // 가짜 회원 ID를 추가
        return new AccessTokenResponse(stubMemberId, STUB_MEMBER_REFRESH_인증_코드);
    }

    public static TokenRenewalRequest MEMBER_리뉴얼_토큰_요청() {
        return new TokenRenewalRequest(더미_리프레시_토큰);
    }

    public static AccessTokenResponse MEMBER_리뉴얼_토큰_응답() {
        return new AccessTokenResponse(1L, 더미_엑세스_토큰);
    }

}
