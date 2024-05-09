package com.time.studentmanage.exception;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@Slf4j
@ControllerAdvice
public class RecordExControllerAdvice {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(DateConvertException.class)
    public String handleDateConvertException(DateConvertException ex, HttpServletRequest request, Model model) {
        String[] requestURIBits = request.getRequestURI().split("/list");

        StringBuffer backURI = new StringBuffer();
        for (String s : requestURIBits) {
            backURI.append(s);
        }

        model.addAttribute("errorMessage", ex.getMessage());
        model.addAttribute("buttonName", "피드백 목록가기");
        model.addAttribute("backLink", backURI.toString());
        return "error/400";
    }
}
