package com.time.studentmanage.domain.dto.teacher;

import com.querydsl.core.annotations.QueryProjection;
import com.time.studentmanage.domain.enums.GenderType;
import com.time.studentmanage.domain.enums.MemberType;
import com.time.studentmanage.domain.enums.Position;
import com.time.studentmanage.domain.member.Teacher;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TeacherRespDto {
    private Long id;
    private String name;
    private String phoneNumber;
    private String email;
    private Position position;
    private MemberType memberType;
    private GenderType gender;

    @QueryProjection
    public TeacherRespDto(Teacher teacher) {
        this.id = teacher.getId();
        this.name = teacher.getName();
        this.phoneNumber = teacher.getPhoneNumber();
        this.email = teacher.getEmail();
        this.position = teacher.getPosition();
        this.memberType = teacher.getMemberType();
        this.gender = teacher.getGender();
    }
}
