package com.time.studentmanage.domain.dto.classroom;

import com.time.studentmanage.domain.enums.ClassStatus;
import com.time.studentmanage.domain.enums.ClassType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

//@Getter
//@Setter
@Data
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
