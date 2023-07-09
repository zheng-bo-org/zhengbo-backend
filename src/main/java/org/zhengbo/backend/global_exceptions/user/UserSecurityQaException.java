package org.zhengbo.backend.global_exceptions.user;

import org.springframework.http.HttpStatus;
import org.zhengbo.backend.global_exceptions.BaseGlobalException;
import org.zhengbo.backend.global_exceptions.GlobalException;

@GlobalException.MessageAbleException
public class UserSecurityQaException extends BaseGlobalException {
    public UserSecurityQaException(UserSecurityQaExceptionCode code, HttpStatus statusCode) {
        super(code.name(), statusCode);
    }

    @MessagesInEnum
    public enum UserSecurityQaExceptionCode {
        @EnumMsgDesc(desc = "The question is not supported for now.")
        NOT_SUPPORTED_QUESTION,

        @EnumMsgDesc(desc = "The answer of the question is invalid.")
        INVALID_ANSWER,

        @EnumMsgDesc(desc = "The qa length must be 3")
        BAD_QA_LENGTH
    }
}
