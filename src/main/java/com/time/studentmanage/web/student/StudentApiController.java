package com.time.studentmanage.web.student;

import com.time.studentmanage.service.StudentService;
import jakarta.validation.constraints.Pattern;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.BadRequestException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class StudentApiController {
    private final StudentService studentService;
    @GetMapping("/id/check")
    public ResponseEntity<?> checkIdDuplication(@RequestParam(value = "userId") String userId) throws BadRequestException{
        log.info("userId={}", userId);

        boolean regexResult = userId.matches("^[a-z0-9]{6,20}$");


        if (!regexResult) {
            if (userId.length() < 6) {
                throw new BadRequestException("아이디 길이가 6자 미만입니다.");
            }
            throw new BadRequestException("아이디 형식을 확인해주세요.");
        }

        if (studentService.checkIdDuplication(userId)) {
            throw new BadRequestException("이미 사용 중인 아이디입니다.");
        } else {
            return ResponseEntity.ok("사용 가능한 아이디입니다.");
        }
    }
}
