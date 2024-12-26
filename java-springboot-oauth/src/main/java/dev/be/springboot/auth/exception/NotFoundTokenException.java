package dev.be.springboot.auth.exception;

import dev.be.springboot.global.error.CustomException;
import dev.be.springboot.global.error.ErrorCode;
import lombok.Getter;

@Getter
public class NotFoundTokenException extends CustomException {

    public NotFoundTokenException(final ErrorCode errorCode, final String message) {
        // 존재하지 않는 Token 입니다.
        super(errorCode, message);
    }
}
