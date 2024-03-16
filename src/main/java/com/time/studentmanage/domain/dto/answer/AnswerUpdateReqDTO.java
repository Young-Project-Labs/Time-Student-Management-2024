package com.time.studentmanage.domain.dto.answer;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AnswerUpdateReqDTO {

    private Long answerId;
    private String content;

}
