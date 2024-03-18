package com.time.studentmanage.domain.dto.record;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class RecordRespDTO {
    private Long id;
    private String content;
    private String teacherName;

    private LocalDateTime lastModifiedDate;

    public RecordRespDTO(Long id, String content, String teacherName, LocalDateTime lastModifiedDate) {
        this.id = id;
        this.content = content;
        this.teacherName = teacherName;
        this.lastModifiedDate = lastModifiedDate;
    }
}
