package dev.be.springboot.auth.exception;

import dev.be.springboot.global.error.CustomException;
import dev.be.springboot.global.error.ErrorCode;
import lombok.Getter;

@Getter
public class ServerErrorOAuthException extends CustomException {

    // OAuth 서버와의 통신 과정에서 문제가 발생했습니다.
    public ServerErrorOAuthException() {
        super(ErrorCode.SERVER_ERROR_OAUTH_TOKEN);
    }

    public ServerErrorOAuthException(final String message) {
        super(ErrorCode.SERVER_ERROR_OAUTH_TOKEN, message);
    }
}
