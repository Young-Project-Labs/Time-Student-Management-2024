package com.time.studentmanage.domain.dto.record;

import com.time.studentmanage.domain.enums.RecordStatus;
import com.time.studentmanage.domain.member.Student;
import com.time.studentmanage.domain.member.Teacher;
import com.time.studentmanage.domain.record.Record;
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
    private String title;
    @NotBlank
    private String content;

    public RecordSaveReqDto(Long studentId, Long teacherId, String title, String content) {
        this.studentId = studentId;
        this.teacherId = teacherId;
        this.title = title;
        this.content = content;
    }

    public Record toEntity(Teacher teacher, Student student, String title, String content) {
        return Record.builder()
                .teacher(teacher)
                .student(student)
                .title(title)
                .content(content)
                .view(0)
                .status(RecordStatus.PUBLISHED)
                .build();
    }


    @Override
    public String toString() {
        return "RecordSaveReqDto{" +
                "studentId=" + studentId +
                ", teacherId=" + teacherId +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                '}';
    }
}
