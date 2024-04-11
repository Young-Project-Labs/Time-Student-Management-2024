package com.time.studentmanage.domain.dto.student;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class SearchStudentRespDto {
    private Long id;
    private String name;
    private String schoolName;
    private int grade;
}
