package com.time.studentmanage.domain.dto.record;

import com.time.studentmanage.domain.enums.RecordStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class RecordRespDto {
    private Long recordId;
    private String content;
    private String teacherName;
    private String studentName;
    private RecordStatus status;

    private LocalDateTime createDate;
    private LocalDateTime lastModifiedDate;

    public RecordRespDto(Long recordId, String content, String teacherName, String studentName, RecordStatus status, LocalDateTime createDate, LocalDateTime lastModifiedDate) {
        this.recordId = recordId;
        this.content = content;
        this.teacherName = teacherName;
        this.studentName = studentName;
        this.status = status;
        this.createDate = createDate;
        this.lastModifiedDate = lastModifiedDate;
    }

    @Override
    public String toString() {
        return "RecordRespDto{" +
                "recordId=" + recordId +
                ", content='" + content + '\'' +
                ", teacherName='" + teacherName + '\'' +
                ", studentName='" + studentName + '\'' +
                ", status=" + status +
                ", createDate=" + createDate +
                ", lastModifiedDate=" + lastModifiedDate +
                '}';
    }
}
