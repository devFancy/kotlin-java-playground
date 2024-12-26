package dev.be.springboot.infrastructure.oauth.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.be.springboot.auth.dto.OAuthMember;
import dev.be.springboot.auth.exception.ServerErrorOAuthException;
import dev.be.springboot.auth.service.OAuthClient;
import dev.be.springboot.global.config.oauth.KakaoProperties;
import dev.be.springboot.infrastructure.oauth.dto.KakaoTokenResponse;
import dev.be.springboot.infrastructure.oauth.dto.KakaoUserInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Component
public class KakaoOAuthClient implements OAuthClient {

    private static final String JWT_DELIMITER = "\\.";
    private static final String KAKAO = "kakao";

    private final KakaoProperties properties;
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    public KakaoOAuthClient(final KakaoProperties kakaoProperties,
                            final RestTemplateBuilder restTemplateBuilder,
                            final ObjectMapper objectMapper) {
        this.properties = kakaoProperties;
        this.restTemplate = restTemplateBuilder.build();
        this.objectMapper = objectMapper;
    }

    @Override
    public OAuthMember getOAuthMember(final String code, final String redirectUri) {
        KakaoTokenResponse kakaoTokenResponse = requestKakaoToken(code, redirectUri);
        log.info("kakao accessToken response: {}", kakaoTokenResponse.getAccessToken());
        KakaoUserInfo kakaoUserInfo = requestUserInfo(kakaoTokenResponse.getAccessToken());
        log.info("userInfo.getEmail(): {}", kakaoUserInfo.getKakao_account().getEmail());
        log.info("userInfo.getName(): {}", kakaoUserInfo.getKakao_account().getProfile().getNickname());

        String refreshToken = kakaoTokenResponse.getAccessToken();
        return new OAuthMember(kakaoUserInfo.getKakao_account().getEmail(), kakaoUserInfo.getKakao_account().getProfile().getNickname(), refreshToken);
    }

    private KakaoTokenResponse requestKakaoToken(final String code, final String redirectUri) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");
        MultiValueMap<String, String> params = generateTokenParams(code, redirectUri);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);
        return fetchKakaoToken(request).getBody();

    }

    private MultiValueMap<String, String> generateTokenParams(final String code, final String redirectUri) {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", properties.getClientId());
        params.add("redirect_uri", redirectUri);
        params.add("code", code);
        params.add("client_secret", properties.getClientSecret());
        return params;
    }

    private ResponseEntity<KakaoTokenResponse> fetchKakaoToken(final HttpEntity<MultiValueMap<String, String>> request) {
        try {
            return restTemplate.postForEntity(properties.getTokenUri(), request, KakaoTokenResponse.class);
        } catch (final RestClientException e) {
            throw new ServerErrorOAuthException();
        }
    }

    private KakaoUserInfo requestUserInfo(final String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);
        headers.add("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");
        HttpEntity<String> request = new HttpEntity<>(headers);

        try {
            ResponseEntity<String> response = restTemplate.exchange(properties.getUserInfoUri(), HttpMethod.GET, request, String.class);
            // ObjectMapper를 사용하여 JSON 응답을 KakaoUserInfo 객체로 변환
            return objectMapper.readValue(response.getBody(), KakaoUserInfo.class);
        } catch (RestClientException | JsonProcessingException e) {
            throw new ServerErrorOAuthException("Failed to fetch user info");
        }
    }

    @Override
    public String getProviderName() {
        return KAKAO;
    }
}
