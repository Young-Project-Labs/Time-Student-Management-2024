package com.time.studentmanage.domain.dto.student;

import com.time.studentmanage.domain.enums.SearchType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class SearchReqDto {

    private SearchType searchType;
    private String content;
    private int page;
}