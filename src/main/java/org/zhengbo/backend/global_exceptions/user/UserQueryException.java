package org.zhengbo.backend.global_exceptions.user;

import org.springframework.http.HttpStatus;
import org.zhengbo.backend.global_exceptions.BaseGlobalException;
import org.zhengbo.backend.global_exceptions.GlobalException;

@GlobalException.MessageAbleException
public class UserQueryException extends BaseGlobalException {
    public UserQueryException(UserQueryExceptionCode code, HttpStatus statusCode) {
        super(code.name(), statusCode);
    }

    @MessagesInEnum
    public enum UserQueryExceptionCode {
        @EnumMsgDesc(desc = "User not exists.")
        NO_SUCH_USER
    }
}
