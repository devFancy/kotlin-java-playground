package dev.be.javaspringbootoauth.member.exception;

import dev.be.javaspringbootoauth.global.error.CustomException;
import dev.be.javaspringbootoauth.global.error.ErrorCode;
import lombok.Getter;

@Getter
public class InvalidMemberException extends CustomException {


    public InvalidMemberException() {
        super(ErrorCode.INVALID_MEMBER);
    }

    // 잘못된 회원의 정보입니다.
    public InvalidMemberException(final String message) {
        super(ErrorCode.INVALID_MEMBER, message);
    }
}
