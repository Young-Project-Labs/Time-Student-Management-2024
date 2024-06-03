package com.time.studentmanage.domain.dto.record;

import com.querydsl.core.annotations.QueryProjection;
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
    private String title;
    private String content;
    private String teacherName;
    private String studentName;
    private RecordStatus status;
    private int view;

    private LocalDateTime createDate;
    private LocalDateTime lastModifiedDate;

    @QueryProjection
    public RecordRespDto(Long recordId, String title, String content, String teacherName, String studentName, RecordStatus status, int view, LocalDateTime createDate, LocalDateTime lastModifiedDate) {
        this.recordId = recordId;
        this.title = title;
        this.content = content;
        this.teacherName = teacherName;
        this.studentName = studentName;
        this.status = status;
        this.view = view;
        this.createDate = createDate;
        this.lastModifiedDate = lastModifiedDate;
    }

    @Override
    public String toString() {
        return "RecordRespDto{" +
                "recordId=" + recordId +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", teacherName='" + teacherName + '\'' +
                ", studentName='" + studentName + '\'' +
                ", status=" + status +
                ", view=" + view +
                ", createDate=" + createDate +
                ", lastModifiedDate=" + lastModifiedDate +
                '}';
    }
}
