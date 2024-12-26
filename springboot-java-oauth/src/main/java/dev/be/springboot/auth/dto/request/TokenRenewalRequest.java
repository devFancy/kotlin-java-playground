package dev.be.springboot.auth.dto.request;

import lombok.Getter;

import javax.validation.constraints.NotNull;

@Getter
public class TokenRenewalRequest {

    @NotNull(message = "리프레시 토큰은 공백일 수 없습니다")
    private String refreshToken;

    private TokenRenewalRequest() {
    }

    public TokenRenewalRequest(final String refreshToken) {
        this.refreshToken = refreshToken;
    }
}
