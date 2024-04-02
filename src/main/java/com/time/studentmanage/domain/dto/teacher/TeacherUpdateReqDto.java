package com.time.studentmanage.domain.dto.teacher;

import com.time.studentmanage.domain.enums.GenderType;
import com.time.studentmanage.domain.enums.MemberType;
import com.time.studentmanage.domain.enums.Position;
import com.time.studentmanage.domain.member.Teacher;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class TeacherUpdateReqDto {
    private Long id;
    private String name;
    private String phoneNumber;
    private String email;
    private Position position;
    private MemberType memberType;
    private GenderType gender;

    @Builder
    public TeacherUpdateReqDto(Long id, String name, String phoneNumber, String email, Position position, MemberType memberType, GenderType gender) {
        this.id = id;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.position = position;
        this.memberType = memberType;
        this.gender = gender;
    }
    //Dto -> Student 엔티티 변환
    public Teacher toEntity() {
        Teacher teacher = Teacher.builder()
                .name(name)
                .phoneNumber(phoneNumber)
                .email(email)
                .position(position)
                .memberType(memberType)
                .gender(gender)
                .build();
        return teacher;
    }
}
