package com.time.studentmanage.domain.dto.student;

import com.querydsl.core.annotations.QueryProjection;
import com.time.studentmanage.domain.enums.AttendanceStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class StudentSearchRespDto {
    private Long id;
    private String name;
    private String phoneNumber;
    private String schoolName;
    private int grade;
    private String parentName;
    private String parentPhoneNumber;
    private AttendanceStatus attendanceStatus;
    private LocalDateTime createDate; // 학원 등록일
    private LocalDateTime quitDate; // 퇴원 일자

    @QueryProjection
    public StudentSearchRespDto(Long id, String name, String phoneNumber, String schoolName, int grade, String parentName, String parentPhoneNumber, AttendanceStatus attendanceStatus, LocalDateTime createDate, LocalDateTime quitDate) {
        this.id = id;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.schoolName = schoolName;
        this.grade = grade;
        this.parentName = parentName;
        this.parentPhoneNumber = parentPhoneNumber;
        this.attendanceStatus = attendanceStatus;
        this.createDate = createDate;
        this.quitDate = quitDate;
    }
}
