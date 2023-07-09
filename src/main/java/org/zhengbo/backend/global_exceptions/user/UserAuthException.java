package org.zhengbo.backend.global_exceptions.user;

import org.springframework.http.HttpStatus;
import org.zhengbo.backend.global_exceptions.BaseGlobalException;
import org.zhengbo.backend.global_exceptions.GlobalException;

@GlobalException.MessageAbleException
public class UserAuthException extends BaseGlobalException {
    @MessagesInEnum
    public enum UserAuthExceptionCode{
        @EnumMsgDesc(desc = "Duplicated username on user sign up.")
        USERNAME_DUPLICATED,

        @EnumMsgDesc(desc = "User not found")
        USER_NOT_FOUND,

        @EnumMsgDesc(desc = "Incorrect password")
        INCORRECT_PASSWORD,

        @EnumMsgDesc(desc = "User not logged in")
        UN_AUTHENTICATED,

        @EnumMsgDesc(desc = "User name too short, the length of the username must >= 4")
        USERNAME_TOO_SHORT
    }


    public UserAuthException(UserAuthExceptionCode code, HttpStatus statusCode) {
        super(code.name(), statusCode);
    }
}