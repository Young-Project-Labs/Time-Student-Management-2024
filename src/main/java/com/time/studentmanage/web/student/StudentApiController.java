package com.time.studentmanage.web.student;

import com.time.studentmanage.service.StudentService;
import jakarta.validation.constraints.Pattern;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.BadRequestException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class StudentApiController {
    private final StudentService studentService;
    @GetMapping("/id/check")
    public ResponseEntity<?> checkIdDuplication(@RequestParam(value = "userId") String userId){
        log.info("userId={}", userId);

        studentService.checkIdDuplication(userId);

        return ResponseEntity.ok("사용 가능한 아이디입니다.");
    }
}
