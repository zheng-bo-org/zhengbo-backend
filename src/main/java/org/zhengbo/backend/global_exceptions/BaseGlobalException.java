package org.zhengbo.backend.global_exceptions;

import org.springframework.http.HttpStatus;

public class BaseGlobalException extends RuntimeException implements GlobalException{
    private final String code;
    private final HttpStatus statusCode;

    public BaseGlobalException(String code, HttpStatus statusCode) {
        this.code = code;
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
