package dev.be.springboot.auth.repository;

import dev.be.springboot.auth.exception.NotFoundTokenException;
import dev.be.springboot.global.error.ErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
public class InMemoryAuthTokenRepository implements TokenRepository {

    private static final Map<Long, String> TOKEN_REPOSITORY = new ConcurrentHashMap<>();

    @Override
    public String save(final Long memberId, final String refreshToken) {
        TOKEN_REPOSITORY.put(memberId, refreshToken);
        return TOKEN_REPOSITORY.get(memberId);
    }

    @Override
    public boolean exist(final Long memberId) {
        return TOKEN_REPOSITORY.containsKey(memberId);
    }

    @Override
    public String getToken(final Long memberId) {
        Optional<String> token = Optional.ofNullable(TOKEN_REPOSITORY.get(memberId));
        if (token.isEmpty()) {
            log.error("토큰을 찾을 수 없습니다. memberId: {}", memberId);
        }
        return token.orElseThrow(() -> new NotFoundTokenException(ErrorCode.NOT_FOUND_TOKEN, "일치하는 토큰이 존재하지 않습니다."));
    }

    @Override
    public void deleteAll() {
        TOKEN_REPOSITORY.clear();
    }
}