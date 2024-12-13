package dev.be.javaspringbootoauth.infrastructure.client;


import dev.be.javaspringbootoauth.common.OAuthFixtures;
import dev.be.javaspringbootoauth.auth.dto.OAuthMember;
import dev.be.javaspringbootoauth.auth.service.OAuthClient;

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
