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

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class StudentUpdateReqDto {
    private Long id;
    @NotBlank
    private String name;
    @NotBlank
    private String userId;
    @NotBlank
    @Pattern(regexp = "^(010|011|016|017|018|019)-\\d{3,4}-\\d{4}$")
    private String phoneNumber;
    @NotBlank
    private String schoolName;
    @NotBlank
    private String parentName;
    @NotBlank
    @Pattern(regexp = "^(010|011|016|017|018|019)-\\d{3,4}-\\d{4}$")
    private String parentPhoneNumber;
    @NotNull
    @Min(1)
    @Max(6)
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

    //StudentRespDto -> StudentUpdateReqDto 생성자
    public StudentUpdateReqDto(StudentRespDto studentRespDto) {
        this.id = studentRespDto.getId();
        this.name = studentRespDto.getName();
        this.userId = studentRespDto.getUserId();
        this.phoneNumber = studentRespDto.getPhoneNumber();
        this.schoolName = studentRespDto.getSchoolName();
        this.parentName = studentRespDto.getParentName();
        this.parentPhoneNumber = studentRespDto.getParentPhoneNumber();
        this.grade = studentRespDto.getGrade();
        this.attendanceStatus = studentRespDto.getAttendanceStatus();
        this.memberType = studentRespDto.getMemberType();
        this.gender = studentRespDto.getGender();
        this.classType = studentRespDto.getClassType();
        this.address = studentRespDto.getAddress();
    }

    //Dto -> Student 엔티티 변환
    public Student toEntity() {
        Student student = Student.builder()
                .id(id)
                .name(name)
                .userId(userId)
                .phoneNumber(phoneNumber)
                .schoolName(schoolName)
                .parentName(parentName)
                .parentPhoneNumber(parentPhoneNumber)
                .grade(grade)
                .attendanceStatus(attendanceStatus)
                .memberType(memberType)
                .gender(gender)
                .classType(classType)
                .address(address)
                .build();
        return student;
    }
}
