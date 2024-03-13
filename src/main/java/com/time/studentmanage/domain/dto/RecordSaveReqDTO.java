package com.time.studentmanage.domain.dto;

import com.time.studentmanage.domain.Records;
import com.time.studentmanage.domain.member.Student;
import com.time.studentmanage.domain.member.Teacher;
import lombok.Builder;
import lombok.Getter;

@Getter
public class RecordSaveReqDTO {
    private Long studentId;
    private Long teacherId;
    private String content;

    @Builder
    public RecordSaveReqDTO(Long studentId, Long teacherId, String content) {
        this.studentId = studentId;
        this.teacherId = teacherId;
        this.content = content;
    }

    public Records toEntity(Teacher teacher, Student student, String content) {
        return Records.builder()
                .teacher(teacher)
                .student(student)
                .content(content)
                .build();
    }
}
