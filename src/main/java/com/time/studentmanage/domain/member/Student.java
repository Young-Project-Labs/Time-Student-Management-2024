package com.time.studentmanage.domain.member;

import com.time.studentmanage.domain.Address;
import com.time.studentmanage.domain.Records;
import com.time.studentmanage.domain.enums.AttendanceStatus;
import com.time.studentmanage.domain.enums.ClassType;
import com.time.studentmanage.domain.enums.GenderType;
import com.time.studentmanage.domain.enums.MemberType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.FetchType.*;

@Entity
@Getter
@Setter
public class Student extends BaseMemberEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "student_id")
    private Long id;

    private String userId;
    private String password;
    private String name;
    private String phoneNumber;
    private String schoolName;
    private int grade;

    @Enumerated(EnumType.STRING)
    private AttendanceStatus attendanceStatus;

    @Enumerated(EnumType.STRING)
    private MemberType memberType;

    @Enumerated(EnumType.STRING)
    private GenderType gender;

    @Enumerated(EnumType.STRING)
    private ClassType classType;

    @Embedded
    private Address address;

    @OneToMany(mappedBy = "student")
    private List<Parent> parentList = new ArrayList<>();

    @OneToMany(mappedBy = "student")
    private List<Records> recordsList = new ArrayList<>();

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "teacher_id")
    private Teacher teacher;
    

    protected Student() {
    }

    public Student(String name, String userId, String password,String phoneNumber, String schoolName, ClassType classType, int grade, MemberType memberType, GenderType gender, Address address, AttendanceStatus attendanceStatus) {
        this.name = name;
        this.userId = userId;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.schoolName = schoolName;
        this.grade = grade;
        this.attendanceStatus = attendanceStatus;
        this.memberType = memberType;
        this.gender = gender;
        this.classType = classType;
        this.address = address;
    }
}
