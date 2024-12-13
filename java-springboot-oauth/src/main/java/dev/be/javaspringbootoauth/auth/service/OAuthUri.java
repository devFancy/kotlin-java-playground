package dev.be.javaspringbootoauth.auth.service;

public interface OAuthUri {

    String generate(final String redirectUri);

    String getProviderName();
}
