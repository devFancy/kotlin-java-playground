package dev.be.springboot.config;

import dev.be.springboot.infrastructure.client.StubOAuthClient;
import dev.be.springboot.infrastructure.uri.StubOAuthUri;
import dev.be.springboot.auth.service.OAuthClient;
import dev.be.springboot.auth.service.OAuthUri;
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
