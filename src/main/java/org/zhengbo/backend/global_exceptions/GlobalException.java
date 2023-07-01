package org.zhengbo.backend.global_exceptions;

import org.hibernate.Internal;
import org.springframework.http.HttpStatus;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

public interface GlobalException {


    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.TYPE)
    @interface MessageAbleException {
        String name() default "";
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.TYPE)
    @interface MessagesInEnum {

    }


    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.FIELD)
    @interface EnumMsgDesc {
        String desc();
    }



    String getCode();

    HttpStatus getStatusCode();
}
