package dev.be.springboot.infrastructure.uri;


import dev.be.springboot.auth.service.OAuthUri;

public class StubOAuthUri implements OAuthUri {

    @Override
    public String generate(final String redirectUri) {
        return "https://localhost:3000";
    }

    @Override
    public String getProviderName() {
        return "";
    }
}
