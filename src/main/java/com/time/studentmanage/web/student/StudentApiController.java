package com.time.studentmanage.web.student;

import com.time.studentmanage.domain.dto.student.StudentUpdatePwdReqDto;
import com.time.studentmanage.service.StudentService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.BadRequestException;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Slf4j
public class StudentApiController {
    private final StudentService studentService;

    //아이디 중복 조회
    @GetMapping("/student/id")
    public ResponseEntity<?> checkIdDuplication(@RequestParam(value = "userId") String userId){
        log.info("userId={}", userId);

        studentService.checkIdDuplication(userId);

        return ResponseEntity.ok("사용 가능한 아이디입니다.");
    }

    //패스워드 변경
    @PutMapping("/student/password")
    public ResponseEntity<String> changePassword(@RequestBody @Validated StudentUpdatePwdReqDto updatePwdReqDto, HttpSession session) {
        log.info("updatePwdReqDto={}", updatePwdReqDto);

        studentService.updatePwd(updatePwdReqDto.getStudentId(), updatePwdReqDto.getPassword());

        //비밀번호 변경이 성공적으로 처리 되면 세션을 제거
        session.invalidate();
        return ResponseEntity.ok("패스워드가 변경되었습니다. 다시 로그인 해주세요.");
    }


}
