package com.time.studentmanage.web.student;

import com.time.studentmanage.domain.dto.classroom.ClassStudentRespDto;
import com.time.studentmanage.domain.dto.student.*;
import com.time.studentmanage.domain.enums.SearchType;
import com.time.studentmanage.exception.DataNotFoundException;
import com.time.studentmanage.service.StudentService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
public class StudentApiController {
    private final StudentService studentService;

    //아이디 중복 조회
    @GetMapping("/student/id")
    public ResponseEntity<?> checkIdDuplication(@RequestParam(value = "userId") String userId) {
        log.info("userId={}", userId);

        studentService.checkIdDuplication(userId);

        return ResponseEntity.ok("사용 가능한 아이디입니다.");
    }

    //패스워드 변경(학생 정보 창에서 변경)
    @PutMapping("/student/password")
    public ResponseEntity<String> changePassword(@RequestBody @Validated StudentUpdatePwdReqDto updatePwdReqDto, HttpSession session) {
        log.info("updatePwdReqDto={}", updatePwdReqDto);

        studentService.updatePwd(updatePwdReqDto.getUserId(), updatePwdReqDto.getPassword());

        //비밀번호 변경이 성공적으로 처리 되면 세션을 제거
        session.invalidate();
        return ResponseEntity.ok("패스워드가 변경되었습니다. 다시 로그인 해주세요.");
    }
    //재원여부 변경
    @PutMapping("/student/{id}/attendance")
    public ResponseEntity<?> editAttendance(@PathVariable("id") Long id) {
        studentService.editAttendanceStatus(id);
        return ResponseEntity.ok("재원여부 변경에 성공했습니다.");
    }
    //학생 삭제
    @DeleteMapping("/student/{id}")
    public ResponseEntity<?> deleteStudent(@PathVariable("id") Long id) {
        studentService.deleteStudent(id);
        return ResponseEntity.ok("삭제를 완료했습니다.");
    }

    @GetMapping("/school")
    public ResponseEntity<StudentSearchResult> showStudentList(@RequestParam(value = "schoolName") String schoolName,
                                                               @RequestParam(value = "studentName", required = false) String studentName,
                                                               @RequestParam(value = "page", defaultValue = "0") int page) {

        if (schoolName == null || schoolName.equals("")) {
            throw new IllegalArgumentException("선택된 학교 정보가 없습니다.");
        }

        Page<SelectedSchoolRespDto> respDto = studentService.getHomePageSearchResult(schoolName, studentName, page);

        return ResponseEntity.ok(new StudentSearchResult(respDto, null));
    }


    @GetMapping("/search")
    public ResponseEntity<StudentSearchResult> searchStudent(@RequestParam(value = "schoolName") String schoolName,
                                                             @RequestParam(value = "studentName") String studentName,
                                                             @RequestParam(value = "page", defaultValue = "0") int page) {

        if (studentName.trim().equals("") || studentName == null) {
            throw new IllegalArgumentException("검색어가 입력되지 않았습니다.");
        }

        Page<SelectedSchoolRespDto> searchedResult = studentService.getHomePageSearchResult(schoolName, studentName, page);

        if (searchedResult.isEmpty() || searchedResult == null) {
            throw new DataNotFoundException("결과가 없습니다.");
        }

        return ResponseEntity.ok(new StudentSearchResult(searchedResult, null));
    }

    @GetMapping("/class/student/search")
    public ResponseEntity<StudentSearchResult> searchStudentNotIncludeClassRoom(@RequestParam(value = "content") String content) {

        if (content.trim().equals("") || content == null) {
            throw new IllegalArgumentException("검색어가 입력되지 않았습니다.");
        }

        List<ClassStudentRespDto> studentList = studentService.getSearchedStudentNotIncludeClassRoom(content);
        return ResponseEntity.ok(new StudentSearchResult(studentList, null));
    }
}
