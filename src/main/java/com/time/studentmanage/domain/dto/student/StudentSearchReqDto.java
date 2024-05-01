package com.time.studentmanage.domain.dto.student;

import com.time.studentmanage.domain.enums.SearchType;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class StudentSearchReqDto {

    @NotNull(groups = SearchCheck.class)
    private SearchType searchType;
    private String content;
    private int page;
}
