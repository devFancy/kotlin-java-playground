package dev.be.javaspringbootoauth.auth.repository;

public interface TokenRepository {

    String save(final Long memberId, final String refreshToken);

    boolean exist(final Long memberId);

    String getToken(final Long memberId);

    void deleteAll();
}
