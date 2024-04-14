package com.time.studentmanage.domain.dto.classroom;

import com.time.studentmanage.domain.dto.student.SearchStudentRespDto;
import com.time.studentmanage.domain.enums.ClassType;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ClassRoomBasicInfoDto {
    private String name;
    private String classInfo;
    private ClassType classType;
}
