package dev.be.javaspringbootoauth.auth.controller;

import dev.be.javaspringbootoauth.auth.dto.OAuthMember;
import dev.be.javaspringbootoauth.auth.dto.request.TokenRenewalRequest;
import dev.be.javaspringbootoauth.auth.dto.request.TokenRequest;
import dev.be.javaspringbootoauth.auth.dto.response.AccessTokenResponse;
import dev.be.javaspringbootoauth.auth.dto.response.OAuthUriResponse;
import dev.be.javaspringbootoauth.auth.service.AuthService;
import dev.be.javaspringbootoauth.auth.service.authentication.AuthenticationPrincipal;
import dev.be.javaspringbootoauth.global.ApiResultResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@Slf4j
@Tag(name = "auth", description = "소셜 로그인(구글, 카카오) 관련 API")
@RestController
public class AuthController {

    private final AuthService authService;

    public AuthController(final AuthService authService) {
        this.authService = authService;
    }

    @Operation(summary = "소셜 로그인 유형에 맞는 인가 코드를 조회한다.(Google, Kakao)")
    @GetMapping(value = "/api/v1/auths/{oauthProvider}/oauth-uri")
    public ResponseEntity<ApiResultResponse<OAuthUriResponse>> generateLink(@PathVariable final String oauthProvider, @RequestParam final String redirectUri) {
        log.info("Received request in AuthController with oauthProvider: '{}' and redirectUri: '{}'", oauthProvider, redirectUri);

        String oAuthUri = authService.generateOAuthUri(oauthProvider, redirectUri);
        OAuthUriResponse oAuthUriResponse = new OAuthUriResponse(oAuthUri);
        ApiResultResponse<OAuthUriResponse> apiResultResponse = ApiResultResponse.success(oAuthUriResponse, "auth");
        return new ResponseEntity<>(apiResultResponse, HttpStatus.OK);
    }

    @Operation(summary = "액세스 토큰 및 리프레시 토큰을 동시에 생성한다.")
    @PostMapping(value = "/api/v1/auths/{oauthProvider}/token")
    public ResponseEntity<ApiResultResponse<AccessTokenResponse>> generateAccessAndRefreshToken(
            @PathVariable final String oauthProvider, @Valid @RequestBody final TokenRequest tokenRequest) {
        OAuthMember oAuthMember = authService.handleOAuth(oauthProvider, tokenRequest.getCode(), tokenRequest.getRedirectUri());
        AccessTokenResponse accessAndRefreshTokenResponse = authService.generateAccessAndRefreshToken(oAuthMember);
        ApiResultResponse<AccessTokenResponse> apiResultResponse = ApiResultResponse.success(accessAndRefreshTokenResponse, "auth");
        return new ResponseEntity<>(apiResultResponse, HttpStatus.OK);
    }

    @Operation(summary = "리프레시 토큰을 이용하여 새로운 액세스 토큰을 생성한다.")
    @PostMapping("/api/v1/auths/token/access")
    public ResponseEntity<ApiResultResponse<AccessTokenResponse>> generateAccessAndRefreshToken(@CookieValue("refreshToken") final String refreshToken) {
        TokenRenewalRequest tokenRenewalRequest = new TokenRenewalRequest(refreshToken);
        AccessTokenResponse accessTokenResponse = authService.generateAccessToken(tokenRenewalRequest);
        ApiResultResponse<AccessTokenResponse> apiResultResponse = ApiResultResponse.success(accessTokenResponse, "auth");
        return new ResponseEntity<>(apiResultResponse, HttpStatus.OK);
    }

    @Operation(summary = "사용자가 로그아웃하면 DB에 저장된 리프레시 토큰 값이 제거된다.")
    @DeleteMapping("/api/v1/auths/logout")
    public ResponseEntity<ApiResultResponse<Void>> logout(@AuthenticationPrincipal final Long loginMemberId) {
        authService.deleteToken(loginMemberId);
        ApiResultResponse<Void> apiResultResponse = ApiResultResponse.success(null, "auth");
        return new ResponseEntity<>(apiResultResponse, HttpStatus.NO_CONTENT);
    }
}