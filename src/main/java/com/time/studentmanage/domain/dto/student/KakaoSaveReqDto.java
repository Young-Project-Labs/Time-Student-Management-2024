package com.time.studentmanage.domain.dto.student;

import com.time.studentmanage.domain.enums.AttendanceStatus;
import com.time.studentmanage.domain.enums.ClassType;
import com.time.studentmanage.domain.enums.GenderType;
import com.time.studentmanage.domain.enums.MemberType;
import com.time.studentmanage.domain.member.Address;
import com.time.studentmanage.domain.member.Student;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.*;

import java.util.HashMap;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class KakaoSaveReqDto {
    @NotNull
    private KakaoAccount kakaoAccount; //email만 들어있음.(name phoneNumber gender는 테스트에만 권한이 존재.)
    @NotBlank
    private String name;
    @NotBlank
    private String phoneNumber;
    @NotBlank
    private String parentName;

    @NotBlank
    @Pattern(regexp="^(010|011|016|017|018|019)-\\d{3,4}-\\d{4}$")
    private String parentPhoneNumber;

    @NotBlank
    private String schoolName;

    @NotNull
    @Min(1) @Max(6)
    private Integer grade;

    private AttendanceStatus attendanceStatus;

    @NotNull
    private GenderType gender;

    private MemberType memberType;

    @NotNull
    private ClassType classType;

    @Valid
    private Address address;
    //dto -> student
    public Student toEntity() {
        Student student = Student.builder()
                .name(name)
                .userId(kakaoAccount.getEmail())
                .phoneNumber(phoneNumber)
                .schoolName(schoolName)
                .grade(grade)
                .attendanceStatus(attendanceStatus)
                .memberType(memberType)
                .gender(gender)
                .provider("KAKAO")
                .classType(classType)
                .parentName(parentName)
                .parentPhoneNumber(parentPhoneNumber)
                .address(address)
                .build();
        return student;
    }
}

