package com.time.studentmanage.domain.dto.classroom;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ClassStudentRespDto {
    private Long id;
    private String schoolName;
    private int grade;
    private String name;
    private String phoneNumber;
}
