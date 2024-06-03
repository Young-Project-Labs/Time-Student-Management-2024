package com.time.studentmanage.exception;

import com.time.studentmanage.web.student.StudentController;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice(assignableTypes = StudentController.class)
public class StudentExControllerAdvice {

    /**
     * 해당 예외가 발생했을 때 controller에서 처리해야하는 예외라서 ModelAndView를 반환
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = {IllegalArgumentException.class})
    public String handleIllegalArgumentException(IllegalArgumentException ex, Model model) {
        model.addAttribute("errorMessage", ex.getMessage());
        model.addAttribute("buttonName", "학생 목록가기");
        model.addAttribute("backLink", "/student/list");
        return "error/400";
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = {MethodArgumentNotValidException.class})
    public String handleMethodArgumentNotValidException(Model model) {
        model.addAttribute("errorMessage", "잘못된 페이지 요청입니다.");
        model.addAttribute("buttonName", "학생 목록가기");
        model.addAttribute("backLink", "/student/list");
        return "error/400";
    }
}
