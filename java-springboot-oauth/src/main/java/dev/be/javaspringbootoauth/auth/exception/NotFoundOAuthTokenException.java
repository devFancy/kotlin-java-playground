package dev.be.javaspringbootoauth.auth.exception;

import dev.be.javaspringbootoauth.global.error.CustomException;
import dev.be.javaspringbootoauth.global.error.ErrorCode;
import lombok.Getter;

@Getter
public class NotFoundOAuthTokenException extends CustomException {

    public NotFoundOAuthTokenException() {
        // 존재하지 않는 OAuthToken 입니다.
        super(ErrorCode.NOT_FOUND_OAUTH_TOKEN);
    }

}
