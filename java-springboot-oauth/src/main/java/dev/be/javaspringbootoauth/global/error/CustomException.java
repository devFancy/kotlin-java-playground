package dev.be.javaspringbootoauth.global.error;

import lombok.Getter;

@Getter
public class CustomException extends RuntimeException {

    private final ErrorCode errorCode;

    public CustomException(final ErrorCode errorCode, final String message) {
        super(message);
        this.errorCode = errorCode;
    }

    public CustomException(final ErrorCode errorCode) {
        this(errorCode, errorCode.getMessage());
    }
}
