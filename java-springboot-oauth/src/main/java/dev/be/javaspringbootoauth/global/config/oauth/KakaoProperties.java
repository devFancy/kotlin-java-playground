package dev.be.javaspringbootoauth.global.config.oauth;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

import java.util.List;

@ConfigurationProperties("oauth.kakao")
@ConstructorBinding
public class KakaoProperties {

    private final String clientId;
    private final String clientSecret;
    private final String authorizationEndpoint;
    private final String responseType;
    private final List<String> scopes;
    private final String tokenUri;
    private final String userInfoUri;

    public KakaoProperties(@Value("${oauth.kakao.client-id}") final String clientId,
                           @Value("${oauth.kakao.client-secret}") final String clientSecret,
                           @Value("${oauth.kakao.authorization-endpoint}") final String authorizationEndpoint,
                           @Value("${oauth.kakao.response-type}") final String responseType,
                           @Value("${oauth.kakao.scopes}") final List<String> scopes,
                           @Value("${oauth.kakao.token-uri}") final String tokenUri,
                           @Value("${oauth.kakao.user-info-uri}") final String userInfoUri) {
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.authorizationEndpoint = authorizationEndpoint;
        this.responseType = responseType;
        this.scopes = scopes;
        this.tokenUri = tokenUri;
        this.userInfoUri = userInfoUri;
    }

    public String getClientId() {
        return clientId;
    }

    public String getClientSecret() {
        return clientSecret;
    }

    public String getAuthorizationEndpoint() {
        return authorizationEndpoint;
    }

    public String getResponseType() {
        return responseType;
    }

    public List<String> getScopes() {
        return scopes;
    }

    public String getTokenUri() {
        return tokenUri;
    }

    public String getUserInfoUri() {
        return userInfoUri;
    }
}
