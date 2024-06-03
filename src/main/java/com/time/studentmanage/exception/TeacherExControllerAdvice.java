package com.time.studentmanage.exception;

import com.time.studentmanage.web.student.StudentController;
import com.time.studentmanage.web.teacher.TeacherController;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice(assignableTypes = TeacherController.class)
public class TeacherExControllerAdvice {

    /**
     * 접근 불가 페이지에 접근 시 처리하는 메서드
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = {AccessDeniedException.class})
    public String accessDeniedExceptionHandler(AccessDeniedException ex, Model model) {
        model.addAttribute("errorMessage", ex.getMessage());
        return "error/403";
    }


}
