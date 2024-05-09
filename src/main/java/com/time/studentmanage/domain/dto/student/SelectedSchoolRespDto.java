package com.time.studentmanage.domain.dto.student;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SelectedSchoolRespDto {
    private Long id;
    private String name;
    private int grade;

    @QueryProjection
    public SelectedSchoolRespDto(Long id, String name, int grade) {
        this.id = id;
        this.name = name;
        this.grade = grade;
    }
}
