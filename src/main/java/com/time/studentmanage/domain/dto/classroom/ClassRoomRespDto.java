package com.time.studentmanage.domain.dto.classroom;

import com.querydsl.core.annotations.QueryProjection;
import com.time.studentmanage.domain.enums.ClassType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ClassRoomRespDto {
    private Long classId;
    private String name;
    private String classInfo;
    private ClassType classType;
    private int classSize;

    @QueryProjection
    public ClassRoomRespDto(Long classId, String name, String classInfo, ClassType classType, int classSize) {
        this.classId = classId;
        this.name = name;
        this.classInfo = classInfo;
        this.classType = classType;
        this.classSize = classSize;
    }
}
