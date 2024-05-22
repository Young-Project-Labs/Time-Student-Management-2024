package com.time.studentmanage.domain.dto.student;

import com.time.studentmanage.domain.member.Address;
import com.time.studentmanage.domain.enums.AttendanceStatus;
import com.time.studentmanage.domain.enums.ClassType;
import com.time.studentmanage.domain.enums.GenderType;
import com.time.studentmanage.domain.enums.MemberType;
import com.time.studentmanage.domain.member.Student;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class StudentRespDto {
    private Long id;
    @NotBlank
    private String name;
    @NotBlank
    private String userId;
    private String password;
    @NotBlank
    @Pattern(regexp="^(010|011|016|017|018|019)-\\d{3,4}-\\d{4}$")
    private String phoneNumber;
    @NotBlank
    private String schoolName;
    @NotBlank
    private String parentName;
    @NotBlank
    @Pattern(regexp="^(010|011|016|017|018|019)-\\d{3,4}-\\d{4}$")
    private String parentPhoneNumber;

    private String provider; // 회원가입 유형 ex) KAKAO, GENERAL
    @NotNull
    private Integer grade;
    @NotNull
    private AttendanceStatus attendanceStatus;
    @NotNull
    private MemberType memberType;
    @NotNull
    private GenderType gender;
    @NotNull
    private ClassType classType;
    @Valid
    private Address address;


    public StudentRespDto(Student student) {
        this.id = student.getId();
        this.name = student.getName();
        this.userId = student.getUserId();
        this.password = student.getPassword();
        this.phoneNumber = student.getPhoneNumber();
        this.parentName = student.getParentName();
        this.parentPhoneNumber = student.getParentPhoneNumber();
        this.schoolName = student.getSchoolName();
        this.grade = student.getGrade();
        this.attendanceStatus = student.getAttendanceStatus();
        this.memberType = student.getMemberType();
        this.gender = student.getGender();
        this.classType = student.getClassType();
        this.address = student.getAddress();
    }

}
