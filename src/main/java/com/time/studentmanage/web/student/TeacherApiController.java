package com.time.studentmanage.web.student;

import com.time.studentmanage.service.StudentService;
import com.time.studentmanage.service.TeacherService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class TeacherApiController {
    private final TeacherService teacherService;
    @GetMapping("/teacher/email/check")
    public ResponseEntity<?> checkEmailDuplication(@RequestParam(value = "email") String email){
        // 중복X -> return true;
        teacherService.checkEmailDuplication(email);

        return ResponseEntity.ok("사용 가능한 이메일입니다.");
    }
}
