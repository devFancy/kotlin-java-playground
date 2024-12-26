package dev.be.springboot.infrastructure.client;


import dev.be.springboot.common.OAuthFixtures;
import dev.be.springboot.auth.dto.OAuthMember;
import dev.be.springboot.auth.service.OAuthClient;

public class StubOAuthClient implements OAuthClient {

    @Override
    public OAuthMember getOAuthMember(final String code, final String redirectUri) {
        return OAuthFixtures.parseOAuthMember(code);
    }

    @Override
    public String getProviderName() {
        return "";
    }
}
