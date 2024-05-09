package com.time.studentmanage.exception;

import com.time.studentmanage.domain.dto.student.StudentSearchResult;
import com.time.studentmanage.web.student.StudentApiController;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice(assignableTypes = StudentApiController.class)
public class StudentExRestControllerAdvice {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(IllegalArgumentException.class)
    public ErrorResult illegalExHandle(IllegalArgumentException e) {
        return new ErrorResult("No Args", e.getMessage());
    }

    @ExceptionHandler
    public ResponseEntity<StudentSearchResult> handleNoStudentResult(DataNotFoundException e) {
        ErrorResult errorResult = new ErrorResult("DATA NOT FOUND", e.getMessage());
        return ResponseEntity.ok(new StudentSearchResult(null, errorResult));
    }
}
