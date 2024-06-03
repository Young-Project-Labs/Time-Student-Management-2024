package com.time.studentmanage.exception;

import com.time.studentmanage.web.mail.MailController;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice(assignableTypes = MailController.class)
public class MailExControllerAdvice {
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(DataNotFoundException.class)
    public ErrorResult DataNotFoundExHandle(DataNotFoundException e) {
        return new ErrorResult("No Args", e.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(EmailAuthException.class)
    public ErrorResult emailAuthExHandle(EmailAuthException e) {
        return new ErrorResult("No Args", e.getMessage());
    }

}
