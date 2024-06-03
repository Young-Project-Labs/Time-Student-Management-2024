package com.time.studentmanage.domain.dto.teacher;

import com.time.studentmanage.domain.enums.GenderType;
import com.time.studentmanage.domain.enums.MemberType;
import com.time.studentmanage.domain.enums.Position;
import com.time.studentmanage.domain.member.Teacher;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TeacherUpdateReqDto {
    @NotNull
    private Long id;
    @NotBlank
    private String name;
    @NotBlank
    @Pattern(regexp = "^(010|011|016|017|018|019)-\\d{3,4}-\\d{4}$")
    private String phoneNumber;
    @NotBlank
    private String email;
    @NotNull
    private Position position;
    @NotNull
    private MemberType memberType;
    @NotNull
    private GenderType gender;

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
