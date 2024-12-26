package dev.be.springboot.auth.exception;

import dev.be.springboot.global.error.CustomException;
import dev.be.springboot.global.error.ErrorCode;
import lombok.Getter;

@Getter
public class NotFoundOAuthTokenException extends CustomException {

    public NotFoundOAuthTokenException() {
        // 존재하지 않는 OAuthToken 입니다.
        super(ErrorCode.NOT_FOUND_OAUTH_TOKEN);
    }

}
