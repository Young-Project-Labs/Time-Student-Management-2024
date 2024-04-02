package com.time.studentmanage.domain.dto.teacher;

import com.time.studentmanage.domain.enums.GenderType;
import com.time.studentmanage.domain.enums.MemberType;
import com.time.studentmanage.domain.enums.Position;
import com.time.studentmanage.domain.member.Teacher;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.*;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class TeacherRespDto {
    private Long id;
    private String name;
    private String phoneNumber;
    private String email;

    @Enumerated(EnumType.STRING)
    private Position position;

    @Enumerated(EnumType.STRING)
    private MemberType memberType;

    @Enumerated(EnumType.STRING)
    private GenderType gender;

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
