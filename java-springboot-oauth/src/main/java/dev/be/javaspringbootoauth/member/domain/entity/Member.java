package dev.be.javaspringbootoauth.member.domain.entity;

import dev.be.javaspringbootoauth.global.common.BaseEntity;
import dev.be.javaspringbootoauth.member.domain.SocialType;
import dev.be.javaspringbootoauth.member.exception.InvalidMemberException;
import lombok.Builder;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.EnumSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Table(name = "members")
@Entity
public class Member extends BaseEntity {

    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[a-z0-9._-]+@[a-z]+[.]+[a-z]{2,3}$");

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "nick_name", nullable = false)
    private String nickName;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "social_type", nullable = false)
    private SocialType socialType;

    protected Member() {
    }

    @Builder
    public Member(final String email,
                  final String nickName,
                  final SocialType socialType) {
        validateEmail(email);
        validateSocialType(socialType);

        this.email = email;
        this.nickName = nickName;
        this.socialType = socialType;
    }

    private void validateEmail(final String email) {
        Matcher matcher = EMAIL_PATTERN.matcher(email);
        if (!matcher.matches()) {
            throw new InvalidMemberException("The email format is incorrect."); // 이메일 형식이 올바르지 않습니다.
        }
    }

    private void validateSocialType(final SocialType socialType) {
        if (!EnumSet.of(SocialType.GOOGLE, SocialType.KAKAO).contains(socialType)) {
            throw new InvalidMemberException("This social login type is not supported."); // 해당 소셜 로그인 유형은 지원되지 않습니다.
        }
    }

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getNickName() {
        return nickName;
    }

    public SocialType getSocialType() {
        return socialType;
    }
}
