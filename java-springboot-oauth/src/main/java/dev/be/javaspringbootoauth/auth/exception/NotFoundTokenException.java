package dev.be.javaspringbootoauth.auth.exception;

import dev.be.javaspringbootoauth.global.error.CustomException;
import dev.be.javaspringbootoauth.global.error.ErrorCode;
import lombok.Getter;

@Getter
public class NotFoundTokenException extends CustomException {

    public NotFoundTokenException(final ErrorCode errorCode, final String message) {
        // 존재하지 않는 Token 입니다.
        super(errorCode, message);
    }
}
