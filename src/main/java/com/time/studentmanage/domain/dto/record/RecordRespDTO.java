package com.time.studentmanage.domain.dto.record;

import com.time.studentmanage.domain.Answer;
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
    private List<Answer> answerList;

    private LocalDateTime lastModifiedDate;

    public RecordRespDTO(Long id, String content, String teacherName, List<Answer> answerList, LocalDateTime lastModifiedDate) {
        this.id = id;
        this.content = content;
        this.teacherName = teacherName;
        this.answerList = answerList;
        this.lastModifiedDate = lastModifiedDate;
    }
}
