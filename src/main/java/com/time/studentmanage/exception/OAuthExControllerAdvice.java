package com.time.studentmanage.exception;

import com.time.studentmanage.web.login.OAuthController;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice(assignableTypes = OAuthController.class)
public class OAuthExControllerAdvice {
    @ExceptionHandler(OAuthException.class)
    public String OAuthExHandle(OAuthException e) {
        log.info("OAuth 로그인 과정에서 예외 발생하여 로그인 페이지로 이동.");
        return "redirect:/login";
    }
}
