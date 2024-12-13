package dev.be.javaspringbootoauth.config;

import dev.be.javaspringbootoauth.infrastructure.client.StubOAuthClient;
import dev.be.javaspringbootoauth.infrastructure.uri.StubOAuthUri;
import dev.be.javaspringbootoauth.auth.service.OAuthClient;
import dev.be.javaspringbootoauth.auth.service.OAuthUri;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class ExternalApiConfig {

    @Bean
    public OAuthClient oAuthClient() {
        return new StubOAuthClient();
    }

    @Bean
    public OAuthUri oAuthUri() {
        return new StubOAuthUri();
    }

}
