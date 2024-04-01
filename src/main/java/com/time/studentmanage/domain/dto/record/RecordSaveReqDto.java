package com.time.studentmanage.domain.dto.record;

import com.time.studentmanage.domain.Record;
import com.time.studentmanage.domain.enums.RecordStatus;
import com.time.studentmanage.domain.member.Student;
import com.time.studentmanage.domain.member.Teacher;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class RecordSaveReqDto {

    @NotNull
    private Long studentId;
    @NotNull
    private Long teacherId;
    @NotBlank
    private String content;

    public RecordSaveReqDto(Long studentId, Long teacherId, String content) {
        this.studentId = studentId;
        this.teacherId = teacherId;
        this.content = content;
    }

    public Record toEntity(Teacher teacher, Student student, String content) {
        return Record.builder()
                .teacher(teacher)
                .student(student)
                .content(content)
                .status(RecordStatus.PUBLISHED)
                .build();
    }

    @Override
    public String toString() {
        return "RecordSaveReqDto{" +
                "studentId=" + studentId +
                ", teacherId=" + teacherId +
                ", content='" + content + '\'' +
                '}';
    }
}
