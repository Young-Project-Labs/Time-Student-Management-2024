package com.time.studentmanage.web.student;

import com.time.studentmanage.domain.dto.classroom.ClassStudentRespDto;
import com.time.studentmanage.domain.dto.student.*;
import com.time.studentmanage.service.StudentService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

    //패스워드 변경
    @PutMapping("/student/password")
    public ResponseEntity<String> changePassword(@RequestBody @Validated StudentUpdatePwdReqDto updatePwdReqDto, HttpSession session) {
        log.info("updatePwdReqDto={}", updatePwdReqDto);

        studentService.updatePwd(updatePwdReqDto.getStudentId(), updatePwdReqDto.getPassword());

        //비밀번호 변경이 성공적으로 처리 되면 세션을 제거
        session.invalidate();
        return ResponseEntity.ok("패스워드가 변경되었습니다. 다시 로그인 해주세요.");
    }

    @GetMapping("/school")
    public ResponseEntity<StudentSearchResult> showStudentList(@RequestParam(value = "schoolName") String schoolName) {

        if (schoolName == null || schoolName.equals("")) {
            throw new IllegalArgumentException("선택된 학교 정보가 없습니다.");
        }

        List<SelectedSchoolRespDto> studentList = studentService.getAllStudentsBySchoolName(schoolName);
        return ResponseEntity.ok(new StudentSearchResult(studentList, null));
    }


    @GetMapping("/search")
    public ResponseEntity<StudentSearchResult> searchStudent(@RequestParam(value = "searchType", required = false) String searchType, @RequestParam(value = "content") String content) {

        if (content.trim().equals("") || content == null) {
            throw new IllegalArgumentException("검색어가 입력되지 않았습니다.");
        }
        //searchType 있는 경우 -> 학생 목록에서의 검색 로직
        if (searchType != null) {
            /**
             * 학생 전체 목록(/student)에서 검색바 조회
             * @param searchType -> name, parentName, schoolName
             * @param keyword
             * @return
             */
            log.info("searchStudent 메서드 searchType={}", searchType);
            log.info("searchStudent 메서드 content={}", content);
            if (content.trim().equals("") || content == null) {
                throw new IllegalArgumentException("검색어가 입력되지 않았습니다.");
            }

            List<StudentRespDto> studentRespDtoList = studentService.getSearchedStudentBySearchType(searchType, content);
            return ResponseEntity.ok(new StudentSearchResult(studentRespDtoList, null));
        }

        //searchType 없는 경우 -> 피드백 목록에서의 검색 로직
        List<SearchStudentRespDto> studentList = studentService.getSearchedStudent(content);

        return ResponseEntity.ok(new StudentSearchResult(studentList, null));
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
