package dev.be.javaspringbootoauth.member.exception;

import dev.be.javaspringbootoauth.global.error.CustomException;
import dev.be.javaspringbootoauth.global.error.ErrorCode;
import lombok.Getter;

@Getter
public class NotFoundMemberException extends CustomException {


    public NotFoundMemberException() {
        // 존재하지 않는 회원입니다.
        super(ErrorCode.NOT_FOUND_MEMBER);
    }

    public NotFoundMemberException(final String message) {
        super(ErrorCode.NOT_FOUND_MEMBER, message);
    }
}
