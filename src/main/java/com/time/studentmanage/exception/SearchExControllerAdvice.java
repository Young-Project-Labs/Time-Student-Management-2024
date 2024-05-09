package com.time.studentmanage.exception;

import com.time.studentmanage.web.record.RecordController;
import com.time.studentmanage.web.student.StudentController;
import com.time.studentmanage.web.teacher.TeacherController;
import org.apache.coyote.BadRequestException;
import org.hibernate.TypeMismatchException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice(assignableTypes = {StudentController.class, TeacherController.class, RecordController.class})
public class SearchExControllerAdvice {
//    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ResponseEntity<String> typeMisMatchHandler(MethodArgumentNotValidException e){
        String errorMessage = "검색 과정에서 문제가 발생했습니다.";
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage);
    }
}
