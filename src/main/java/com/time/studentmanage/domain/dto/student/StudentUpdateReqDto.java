package com.time.studentmanage.domain.dto.student;

import com.time.studentmanage.domain.Address;
import com.time.studentmanage.domain.enums.AttendanceStatus;
import com.time.studentmanage.domain.enums.ClassType;
import com.time.studentmanage.domain.enums.GenderType;
import com.time.studentmanage.domain.enums.MemberType;
import com.time.studentmanage.domain.member.Student;
import lombok.*;
import java.time.LocalDateTime;

@Getter
@Setter
public class StudentUpdateReqDto {
    private Long id;
    private String name;
    private String phoneNumber;
    private String schoolName;
    private int grade;
    private AttendanceStatus attendanceStatus;
    private LocalDateTime quitDate;
    private MemberType memberType;
    private GenderType gender;
    private ClassType classType;
    private Address address;

    public StudentUpdateReqDto(Long id, String name, String phoneNumber, String schoolName, int grade, AttendanceStatus attendanceStatus, LocalDateTime quitDate, MemberType memberType, GenderType gender, ClassType classType, Address address) {
        this.id = id;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.schoolName = schoolName;
        this.grade = grade;
        this.attendanceStatus = attendanceStatus;
        this.quitDate = quitDate;
        this.memberType = memberType;
        this.gender = gender;
        this.classType = classType;
        this.address = address;
    }

    //Dto -> Student 엔티티 변환
    public Student toEntity() {
        Student student = Student.builder()
                .name(name)
                .phoneNumber(phoneNumber)
                .schoolName(schoolName)
                .grade(grade)
                .attendanceStatus(attendanceStatus)
                .quitDate(quitDate)
                .memberType(memberType)
                .gender(gender)
                .classType(classType)
                .address(address)
                .build();
        return student;
    }
}
