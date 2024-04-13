package com.time.studentmanage.domain.dto.classroom;

import com.time.studentmanage.domain.enums.ClassType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ClassSaveReqDto {

    @NotBlank
    private String name;
    @NotNull
    private Long teacherId;
    @NotNull
    private ClassType classType;
    @NotBlank
    private String selectedStudents;

    private String classInfo;
}
