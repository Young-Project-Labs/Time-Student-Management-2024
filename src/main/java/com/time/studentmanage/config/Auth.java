package com.time.studentmanage.config;

import com.time.studentmanage.domain.enums.Position;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Auth {
    Role[] role(); //default 값 설정 X



    enum Role {
        TEACHER,
        CHIEF,
        ADMIN;
    }


}
