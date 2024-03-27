package com.time.studentmanage.domain.dto.record;

import com.time.studentmanage.domain.enums.SearchType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class RecordSearchDTO {

    private SearchType searchType; // 검색 타입: [CONTENT, TEACHER_NAME]

    private String content;

    private Long teacherId;

    private Long studentId;

    private String dates;
}