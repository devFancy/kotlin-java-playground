package dev.be.springboot.member.exception;

import dev.be.springboot.global.error.CustomException;
import dev.be.springboot.global.error.ErrorCode;
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
