package dev.be.springboot.member.exception;

import dev.be.springboot.global.error.CustomException;
import dev.be.springboot.global.error.ErrorCode;
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
