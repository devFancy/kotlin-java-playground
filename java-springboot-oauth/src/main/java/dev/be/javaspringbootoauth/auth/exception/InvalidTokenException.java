package dev.be.javaspringbootoauth.auth.exception;

import dev.be.javaspringbootoauth.global.error.CustomException;
import dev.be.javaspringbootoauth.global.error.ErrorCode;
import lombok.Getter;

@Getter
public class InvalidTokenException extends CustomException {

    public InvalidTokenException() {
        super(ErrorCode.INVALID_TOKEN);
    }

    // 유효하지 않는 토큰입니다.
    public InvalidTokenException(final String message) {
        super(ErrorCode.INVALID_TOKEN, message);
    }
}
