package com.time.studentmanage.domain.dto.classroom;

import com.time.studentmanage.domain.enums.ClassType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ClassRoomInfoDto {
    private Long classId;
    private String name;
    private String classInfo;
    private ClassType classType;
    private int classSize;
}
