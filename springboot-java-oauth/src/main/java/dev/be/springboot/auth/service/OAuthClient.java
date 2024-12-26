package dev.be.springboot.auth.service;


import dev.be.springboot.auth.dto.OAuthMember;

public interface OAuthClient {

    OAuthMember getOAuthMember(final String code, final String redirectUri);

    String getProviderName();
}
