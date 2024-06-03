package com.time.studentmanage.domain.dto.record;

import com.time.studentmanage.domain.enums.SearchType;
import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class RecordSearchDto {

    private SearchType searchType; // 검색 타입: [CONTENT, TEACHER_NAME]
    private String content;
    private String dates;
    @Min(0)
    private int page;
}