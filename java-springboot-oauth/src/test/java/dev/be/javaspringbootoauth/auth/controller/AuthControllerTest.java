package dev.be.javaspringbootoauth.auth.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.be.javaspringbootoauth.config.ExternalApiConfig;
import dev.be.javaspringbootoauth.auth.dto.LoginMember;
import dev.be.javaspringbootoauth.auth.service.AuthService;
import dev.be.javaspringbootoauth.auth.service.OAuthUri;
import dev.be.javaspringbootoauth.common.AuthFixtures;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import javax.servlet.http.Cookie;

@Import(ExternalApiConfig.class)
@ActiveProfiles("test")
@WebMvcTest(AuthController.class)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AuthService authService;

    @MockBean
    private OAuthUri oAuthUri;

    public static final String AUTHORIZATION_HEADER_NAME = "Authorization";

    public static final String AUTHORIZATION_HEADER_VALUE = "Bearer aaaaaa.bbbbbb.cccccc";

    @DisplayName("OAuth 구글 소셜 로그인을 위한 링크와 상태코드 200을 반환한다.")
    @Test
    void returnOAuthGoogleSocialLoginLinkAndStatusCode200() throws Exception {
        // given
        BDDMockito.given(oAuthUri.generate(any())).willReturn(AuthFixtures.OAuth_구글_로그인_링크);

        // when & then
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/auths/{oauthProvider}/oauth-uri?redirectUri={redirectUri}"
                        , AuthFixtures.GOOGLE_PROVIDER
                        , "http://localhost:3000/google-callback"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @DisplayName("OAuth 카카오 소셜 로그인을 위한 링크와 상태코드 200을 반환한다.")
    @Test
    void returnOAuthKakaoSocialLoginLinkAndStatusCode200() throws Exception {
        // given
        BDDMockito.given(oAuthUri.generate(any())).willReturn(AuthFixtures.OAuth_카카오_로그인_링크);

        // when & then
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/auths/{oauthProvider}/oauth-uri?redirectUri={redirectUri}"
                        , AuthFixtures.KAKAO_PROVIDER
                        , "http://localhost:3000/kakao-callback"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk());
    }


    @DisplayName("OAuth 구글 로그인을 하면 AccessToken과 RefreshToken, 상태코드 200을 반환한다.")
    @Test
    void returnAccessTokenAndRefreshTokenAndStatusCode200() throws Exception {
        // given
        BDDMockito.given(authService.generateAccessAndRefreshToken(any())).willReturn(AuthFixtures.MEMBER_인증_코드_토큰_응답());

        // when & then
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/auths/{oauthProvider}/token", AuthFixtures.OAUTH_PROVIDER)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(AuthFixtures.MEMBER_인증_코드_토큰_요청())))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @DisplayName("리프레시 토큰을 통해 새로운 액세스 토큰을 발급하면 상태코드 200을 반환한다.")
    @Test
    void createNewAccessTokenFromRefreshTokenAndStatusCode200() throws Exception {
        // given
        BDDMockito.given(authService.generateAccessToken(any())).willReturn(AuthFixtures.MEMBER_리뉴얼_토큰_응답());

        // when & then
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/auths/token/access")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .cookie(new Cookie("refreshToken", "cccccc.bbbbbb.aaaaaa")))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @DisplayName("회원이 로그아웃을 하면 액세스 토큰과 리프레시 토큰 값이 사라지고 204을 반환한다.")
    @Test
    void ifMemberIsLogOutThenDeleteAccessAndRefreshTokenAndStatusCode204() throws Exception {
        // given
        LoginMember loginMember = new LoginMember(1L);
        BDDMockito.willDoNothing().given(authService).deleteToken(loginMember.getId());

        // when & then
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/auths/logout")
                        .header(AUTHORIZATION_HEADER_NAME, AUTHORIZATION_HEADER_VALUE)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .cookie(new Cookie("refreshToken", "cccccc.bbbbbb.aaaaaa")))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNoContent());

        // then

    }
}