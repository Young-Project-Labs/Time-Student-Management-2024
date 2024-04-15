package com.time.studentmanage.web.classroom;

import com.time.studentmanage.domain.classroom.ClassRoom;
import com.time.studentmanage.domain.dto.classroom.ClassRoomStudentUpdateResult;
import com.time.studentmanage.service.ClassRoomService;
import com.time.studentmanage.service.StudentService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
public class ClassRoomApiController {
    private final ClassRoomService classRoomService;
    private final StudentService studentService;

    @Data
    static class StudentIdDto {
        private Long studentId;
    }


    @PostMapping("/class/{classId}/student/list")
    public ResponseEntity addClassRoomStudent(@PathVariable(value = "classId") Long classId,
                                              @RequestBody StudentIdDto studentIdDto) {
        ClassRoom classRoomPs = classRoomService.findById(classId);
        studentService.connectClassRoom(studentIdDto.getStudentId(), classRoomPs);

        return ResponseEntity.ok()
                .body(new ClassRoomStudentUpdateResult("학생 추가 완료"));
    }

}
