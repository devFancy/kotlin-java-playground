package dev.be.springboot.member.domain;


import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum SocialType {
    GOOGLE("google"),
    KAKAO("kakao"),
    NAVER("naver");

    private final String value;
}
