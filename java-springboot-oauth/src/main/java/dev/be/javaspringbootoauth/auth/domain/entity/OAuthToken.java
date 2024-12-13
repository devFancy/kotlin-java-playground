package dev.be.javaspringbootoauth.auth.domain.entity;

import dev.be.javaspringbootoauth.global.common.BaseEntity;
import dev.be.javaspringbootoauth.member.domain.entity.Member;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.util.Objects;

@Table(name = "oauth_tokens")
@Entity
public class OAuthToken extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", referencedColumnName = "id", nullable = false)
    private Member member;

    @Column(name = "refresh_token")
    private String refreshToken;

    protected OAuthToken() {
    }

    public OAuthToken(final Member member, final String refreshToken) {
        this.member = member;
        this.refreshToken = refreshToken;
    }

    public Long getId() {
        return id;
    }

    public Member getMember() {
        return member;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void change(final String refreshToken) {
        if (!Objects.isNull(refreshToken)) {
            this.refreshToken = refreshToken;
        }
    }
}
