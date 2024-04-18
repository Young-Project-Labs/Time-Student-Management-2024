package com.time.studentmanage.domain.dto.record;

import com.time.studentmanage.domain.enums.SearchType;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class RecordSearchDto {

    @NotNull
    private SearchType searchType; // 검색 타입: [CONTENT, TEACHER_NAME]

    private String content;

    private Long studentId;

    private String studentName;

    private String dates;
}