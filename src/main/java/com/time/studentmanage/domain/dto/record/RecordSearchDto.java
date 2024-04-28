package com.time.studentmanage.domain.dto.record;

import com.time.studentmanage.domain.enums.SearchType;
import jakarta.validation.constraints.NotBlank;
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
//    @NotBlank
    private String content;
    @NotNull
    private Long studentId;
    @NotBlank
    private String dates;
//    @NotNull
    private int page;
}