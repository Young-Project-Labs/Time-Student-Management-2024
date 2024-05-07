package com.time.studentmanage.domain.dto.record;

import com.time.studentmanage.domain.enums.SearchType;
import com.time.studentmanage.domain.member.Student;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class RecordSearchReqCondition {
    private Student student;
    private SearchType searchType;
    private String content;
    private LocalDateTime fromDate;
    private LocalDateTime toDate;
    private Pageable pageable;
}
