package com.time.studentmanage.web.student;

import com.time.studentmanage.domain.dto.student.SearchStudentRespDto;
import com.time.studentmanage.domain.dto.student.SelectedSchoolRespDto;
import com.time.studentmanage.domain.dto.student.StudentSearchResult;
import com.time.studentmanage.service.StudentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
public class StudentApiController {
    private final StudentService studentService;

    @GetMapping("/id/check")
    public ResponseEntity<?> checkIdDuplication(@RequestParam(value = "userId") String userId) {
        log.info("userId={}", userId);

        studentService.checkIdDuplication(userId);

        return ResponseEntity.ok("사용 가능한 아이디입니다.");
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
    public ResponseEntity<StudentSearchResult> searchStudent(@RequestParam(value = "content") String content) {

        if (content.trim().equals("") || content == null) {
            throw new IllegalArgumentException("검색어가 입력되지 않았습니다.");
        }

        List<SearchStudentRespDto> studentList = studentService.getSearchedStudent(content);

        return ResponseEntity.ok(new StudentSearchResult(studentList, null));
    }
}
