package org.zhengbo.backend.global_exceptions;

import org.springframework.http.HttpStatus;

@GlobalException.MessageAbleException
public class UserAuthException extends RuntimeException implements GlobalException{
    private final String code;
    private final HttpStatus statusCode;

    @MessagesInEnum
    public enum UserAuthExceptionCode{
        @EnumMsgDesc(desc = "Duplicated username on user sign up.")
        USERNAME_DUPLICATED,

        @EnumMsgDesc(desc = "User not found")
        USER_NOT_FOUND,

        @EnumMsgDesc(desc = "Incorrect password")
        INCORRECT_PASSWORD(),

        @EnumMsgDesc(desc = "User not logged in")
        UN_AUTHENTICATED,

        @EnumMsgDesc(desc = "User name too short, the length of the username must >= 4")
        USERNAME_TOO_SHORT
    }


    public UserAuthException(UserAuthExceptionCode code, HttpStatus statusCode) {
        this.code = code.name();
        this.statusCode = statusCode;
    }

    @Override
    public String getCode() {
        return this.code;
    }

    @Override
    public HttpStatus getStatusCode() {
        return this.statusCode;
    }
}