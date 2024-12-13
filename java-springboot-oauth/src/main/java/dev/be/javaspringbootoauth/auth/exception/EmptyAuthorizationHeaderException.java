package dev.be.javaspringbootoauth.auth.exception;

import dev.be.javaspringbootoauth.global.error.CustomException;
import dev.be.javaspringbootoauth.global.error.ErrorCode;
import lombok.Getter;

@Getter
public class EmptyAuthorizationHeaderException extends CustomException {

    public EmptyAuthorizationHeaderException() {
        super(ErrorCode.EMPTY_AUTHORIZATION_HEADER);
    }

    // Header에 Authorization이 존재하지 않습니다.
    public EmptyAuthorizationHeaderException(final String message) {
        super(ErrorCode.EMPTY_AUTHORIZATION_HEADER, message);
    }
}
