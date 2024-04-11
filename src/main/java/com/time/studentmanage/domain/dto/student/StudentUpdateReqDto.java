package com.time.studentmanage.domain.dto.student;

import com.time.studentmanage.domain.member.Address;
import com.time.studentmanage.domain.enums.AttendanceStatus;
import com.time.studentmanage.domain.enums.ClassType;
import com.time.studentmanage.domain.enums.GenderType;
import com.time.studentmanage.domain.enums.MemberType;
import com.time.studentmanage.domain.member.Student;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class StudentUpdateReqDto {
    private Long id;
    private String name;
    private String userId;
    private String password;
    private String phoneNumber;
    private String schoolName;
    private String parentName;
    private String parentPhoneNumber;
    private Integer grade;
    private AttendanceStatus attendanceStatus;
    private MemberType memberType;
    private GenderType gender;
    private ClassType classType;
    private Address address;

    //StudentRespDto -> StudentUpdateReqDto 생성자
    public StudentUpdateReqDto(StudentRespDto studentRespDto) {
        this.id = studentRespDto.getId();
        this.name = studentRespDto.getName();
        this.userId = studentRespDto.getUserId();
        this.password = studentRespDto.getPassword();
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

    public StudentUpdateReqDto(String name, String phoneNumber, String schoolName, String parentName, String parentPhoneNumber, int grade, AttendanceStatus attendanceStatus, MemberType memberType, GenderType gender, ClassType classType, Address address) {
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.schoolName = schoolName;
        this.parentName = parentName;
        this.parentPhoneNumber = parentPhoneNumber;
        this.grade = grade;
        this.attendanceStatus = attendanceStatus;
        this.memberType = memberType;
        this.gender = gender;
        this.classType = classType;
        this.address = address;
    }

    //Dto -> Student 엔티티 변환
    public Student toEntity() {
        Student student = Student.builder()
                .id(id)
                .name(name)
                .userId(userId)
                .password(password)
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
