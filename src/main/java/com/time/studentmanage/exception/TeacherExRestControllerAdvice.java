package com.time.studentmanage.exception;

import com.time.studentmanage.web.teacher.TeacherApiController;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice(assignableTypes = TeacherApiController.class)
public class TeacherExRestControllerAdvice {
    //IllegalArgumentException(500) -> BAD_REQUEST(400)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(IllegalArgumentException.class)
    public ErrorResult IllegalExHandle(IllegalArgumentException e) {
        log.error("[exceptionHandle] ex", e);
        return new ErrorResult("BAD", e.getMessage());
    }

    /**
     * 접근 불가 페이지에 접근 시 처리하는 메서드
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(AccessDeniedException.class)
    public ErrorResult AccessDenied(AccessDeniedException e) {
        log.error("[exceptionHandle] ex", e);
        return new ErrorResult("ACCESSDENIED", e.getMessage());
    }
}
