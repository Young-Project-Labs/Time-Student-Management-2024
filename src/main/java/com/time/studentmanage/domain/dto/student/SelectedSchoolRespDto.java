package com.time.studentmanage.domain.dto.student;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class SelectedSchoolRespDto {
    private Long id;
    private String name;
    private int grade;
}
