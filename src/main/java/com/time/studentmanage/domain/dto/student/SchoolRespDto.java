package com.time.studentmanage.domain.dto.student;

import com.querydsl.core.annotations.QueryProjection;
import com.time.studentmanage.domain.enums.ClassType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class SchoolRespDto {

    private ClassType classType;
    private String schoolName;

    @QueryProjection
    public SchoolRespDto(ClassType classType, String schoolName) {
        this.classType = classType;
        this.schoolName = schoolName;
    }
}
