package dev.be.springboot.auth.service;

import dev.be.springboot.auth.domain.AuthToken;
import dev.be.springboot.auth.domain.entity.OAuthToken;
import dev.be.springboot.auth.dto.OAuthMember;
import dev.be.springboot.auth.dto.request.TokenRenewalRequest;
import dev.be.springboot.auth.dto.response.AccessTokenResponse;
import dev.be.springboot.auth.event.MemberSavedEvent;
import dev.be.springboot.auth.exception.ServerErrorOAuthException;
import dev.be.springboot.auth.repository.OAuthTokenRepository;
import dev.be.springboot.member.domain.SocialType;
import dev.be.springboot.member.domain.entity.Member;
import dev.be.springboot.member.repository.MemberRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Transactional(readOnly = true)
@Service
public class AuthService {

    private final MemberRepository memberRepository;
    private final ApplicationEventPublisher eventPublisher;
    private final TokenCreator tokenCreator;
    private final OAuthTokenRepository oAuthTokenRepository;
    private final Map<String, OAuthUri> oauthUriProviders;
    private final Map<String, OAuthClient> oauthClients;

    public AuthService(final MemberRepository memberRepository, final ApplicationEventPublisher eventPublisher,
                       final TokenCreator tokenCreator, final OAuthTokenRepository oAuthTokenRepository,
                       final List<OAuthUri> oauthUris, final List<OAuthClient> oauthClients) {
        this.memberRepository = memberRepository;
        this.eventPublisher = eventPublisher;
        this.tokenCreator = tokenCreator;
        this.oAuthTokenRepository = oAuthTokenRepository;
        this.oauthUriProviders = oauthUris.stream()
                .collect(Collectors.toMap(OAuthUri::getProviderName, Function.identity()));
        this.oauthClients = oauthClients.stream()
                .collect(Collectors.toMap(OAuthClient::getProviderName, Function.identity()));
    }

    public String generateOAuthUri(final String providerName, final String redirectUri) {
        log.info("Received request to generate OAuth URI with providerName: '{}' and redirectUri: '{}'", providerName, redirectUri);

        String trimmedProviderName = providerName.trim();
        log.info("Trimmed providerName: '{}'", trimmedProviderName);

        if (!oauthUriProviders.containsKey(trimmedProviderName)) {
            log.error("Not Provided OAuth Provider: {}", providerName);
            log.error("Currently Registered OAuth Providers: {}", oauthUriProviders.keySet());
            throw new ServerErrorOAuthException("This is Not Provided OAuth Provider.");
        }
        return oauthUriProviders.get(trimmedProviderName).generate(redirectUri);
    }

    public OAuthMember handleOAuth(final String providerName, final String code, final String redirectUri) {
        if (!oauthUriProviders.containsKey(providerName)) {
            throw new ServerErrorOAuthException("This is Not Provided OAuth Provider.");
        }
        OAuthClient client = oauthClients.get(providerName);
        OAuthMember oAuthMember = client.getOAuthMember(code, redirectUri);
        SocialType socialType = determineSocialType(providerName);
        return new OAuthMember(oAuthMember.getEmail(), oAuthMember.getNickname(), socialType, oAuthMember.getRefreshToken());
    }

    private SocialType determineSocialType(final String providerName) {
        switch (providerName.toLowerCase()) {
            case "google":
                return SocialType.GOOGLE;
            case "kakao":
                return SocialType.KAKAO;
            default:
                throw new ServerErrorOAuthException(providerName + "is not provide social login.");
        }
    }

    @Transactional
    public AccessTokenResponse generateAccessAndRefreshToken(final OAuthMember oAuthMember) {
        Member foundMember = findMember(oAuthMember);

        OAuthToken oAuthToken = getOAuthToken(oAuthMember, foundMember);
        oAuthToken.change(oAuthMember.getRefreshToken());

        AuthToken authToken = tokenCreator.createAuthToken(foundMember.getId());
        log.info("User's created id: {}", authToken.getId());
        log.info("User's created accessToken: {}", authToken.getAccessToken());
        return new AccessTokenResponse(authToken.getId(), authToken.getAccessToken());
    }

    private Member findMember(final OAuthMember oAuthMember) {
        String email = oAuthMember.getEmail();
        if (memberRepository.existsByEmail(email)) {
            return memberRepository.getByEmail(email);
        }
        return saveMember(oAuthMember);
    }

    private OAuthToken getOAuthToken(final OAuthMember oAuthMember, final Member member) {
        Long memberId = member.getId();
        if (oAuthTokenRepository.existsByMemberId(memberId)) {
            return oAuthTokenRepository.getByMemberId(memberId);
        }
        return oAuthTokenRepository.save(new OAuthToken(member, oAuthMember.getRefreshToken()));
    }

    private Member saveMember(final OAuthMember oAuthMember) {
        Member savedMember = memberRepository.save(oAuthMember.toMember());
        eventPublisher.publishEvent(new MemberSavedEvent(savedMember.getId()));
        return savedMember;
    }

    public Long extractMemberId(final String accessToken) {
        Long memberId = tokenCreator.extractPayLoad(accessToken);
        memberRepository.validateExistById(memberId);
        return memberId;
    }

    public AccessTokenResponse generateAccessToken(final TokenRenewalRequest tokenRenewalRequest) {
        String refreshToken = tokenRenewalRequest.getRefreshToken();
        AuthToken authToken = tokenCreator.renewAuthToken(refreshToken);
        log.info("User's renewed AccessToken: {}", authToken.getAccessToken());
        return new AccessTokenResponse(authToken.getId(), authToken.getAccessToken());
    }

    @Transactional
    public void deleteToken(final Long memberId) {
        oAuthTokenRepository.deleteAllByMemberId(memberId);
    }
}