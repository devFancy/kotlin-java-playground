package dev.be.javaspringbootoauth.auth.service;


import dev.be.javaspringbootoauth.auth.dto.OAuthMember;

public interface OAuthClient {

    OAuthMember getOAuthMember(final String code, final String redirectUri);

    String getProviderName();
}
