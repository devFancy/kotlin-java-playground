package dev.be.springboot.infrastructure.oauth.uri;

import dev.be.springboot.auth.service.OAuthUri;
import dev.be.springboot.global.config.oauth.GoogleProperties;
import org.springframework.stereotype.Component;

@Component
public class GoogleOAuthUri implements OAuthUri {

    private static final String GOOGLE = "google";

    private final GoogleProperties properties;

    public GoogleOAuthUri(final GoogleProperties properties) {
        this.properties = properties;
    }

    @Override
    public String generate(final String redirectUri) {
        return properties.getOauthEndPoint() + "?"
                + "client_id=" + properties.getClientId() + "&"
                + "redirect_uri=" + redirectUri + "&"
                + "response_type=code &"
                + "scope=" + String.join(" ", properties.getScopes()) + "&"
                + "access_type=" + properties.getAccessType() + "&"
                + "prompt=consent"; //개발 환경에서만 사용, 운영 환경에서는 제거
    }

    @Override
    public String getProviderName() {
        return GOOGLE;
    }
}
