package com.time.studentmanage.web.teacher;

import com.time.studentmanage.config.Auth;
import com.time.studentmanage.domain.dto.teacher.TeacherUpdatePwdReqDto;
import com.time.studentmanage.service.StudentService;
import com.time.studentmanage.service.TeacherService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.apache.coyote.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Slf4j
public class TeacherApiController {
    private final TeacherService teacherService;
    //이메일 중복 체크
    @Auth(role = {Auth.Role.CHIEF, Auth.Role.ADMIN})
    @GetMapping("/teacher/email/check")
    public ResponseEntity<?> checkEmailDuplication(@RequestParam(value = "email") String email){
        // 중복X -> return true;
        teacherService.checkEmailDuplication(email);

        return ResponseEntity.ok("사용 가능한 이메일입니다.");
    }

    //선생님 계정 비밀번호 변경
    @Auth(role = {Auth.Role.CHIEF, Auth.Role.ADMIN, Auth.Role.TEACHER})
    @PutMapping("/teacher/password")
    public ResponseEntity<String> changePwd(@Valid @RequestBody TeacherUpdatePwdReqDto updatePwdReqDto, BindingResult bindingResult, HttpSession session) {
        log.info("선생 비밀번호 변경 id={}, password={}", updatePwdReqDto.getTeacherId(),updatePwdReqDto.getPassword());
        teacherService.updatePwd(updatePwdReqDto.getTeacherId(), updatePwdReqDto.getPassword());

        //비밀번호 변경이 성공적으로 처리 되면 세션을 제거
        session.invalidate();
        return ResponseEntity.ok("비밀번호가 변경 되었습니다. 다시 로그인 해주세요.");
    }

}
