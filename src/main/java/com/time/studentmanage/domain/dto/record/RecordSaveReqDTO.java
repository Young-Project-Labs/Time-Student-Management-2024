package com.time.studentmanage.domain.dto.record;

import com.time.studentmanage.domain.Record;
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

    public Record toEntity(Teacher teacher, Student student, String content) {
        return Record.builder()
                .teacher(teacher)
                .student(student)
                .content(content)
                .build();
    }
}
