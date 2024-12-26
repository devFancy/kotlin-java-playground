package dev.be.springboot.auth.service;

public interface OAuthUri {

    String generate(final String redirectUri);

    String getProviderName();
}
