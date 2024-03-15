package com.time.studentmanage.domain.dto.answer;

import com.time.studentmanage.domain.Answer;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AnswerSaveReqDTO {
    private String content;
    private Long parentAnswerId;
    private Long recordId;
    private Long teacherId;

    public Answer toEntity() {
        return Answer.builder()
                .content(this.content)
                .build();
    }
}
