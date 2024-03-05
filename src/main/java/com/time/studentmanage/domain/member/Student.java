package com.time.studentmanage.domain.member;

import com.time.studentmanage.domain.Address;
import com.time.studentmanage.domain.enums.AttendanceStatus;
import com.time.studentmanage.domain.enums.ClassType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
public class Student extends Member {
    private Long grade;
    private String school_name;
    @Enumerated(EnumType.STRING)
    private ClassType classType;
    private String gender;
    @Embedded
    private Address address;
    private AttendanceStatus attendance_status;

    @OneToMany(mappedBy = "student")
    private List<Parent> parentList = new ArrayList<>();

}
