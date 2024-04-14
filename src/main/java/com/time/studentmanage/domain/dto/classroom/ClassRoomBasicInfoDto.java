package com.time.studentmanage.domain.dto.classroom;

import com.time.studentmanage.domain.enums.ClassType;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ClassRoomBasicInfoDto {
    private Long id;
    private String name;
    private String classInfo;
    private ClassType classType;
}
