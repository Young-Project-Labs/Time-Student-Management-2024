package com.time.studentmanage.domain.member;

import com.time.studentmanage.domain.Address;
import com.time.studentmanage.domain.Records;
import com.time.studentmanage.domain.enums.AttendanceStatus;
import com.time.studentmanage.domain.enums.ClassType;
import com.time.studentmanage.domain.enums.GenderType;
import com.time.studentmanage.domain.enums.MemberType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;

import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.FetchType.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DynamicUpdate
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
    private int grade; // TODO: 굳이 Long으로 하지 않아도 될 것 같음

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

    @OneToMany(mappedBy = "student", orphanRemoval = true)
    private List<Parent> parentList = new ArrayList<>();

    @OneToMany(mappedBy = "student")
    private List<Records> recordsList = new ArrayList<>();

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "teacher_id")
    private Teacher teacher;

    @Builder(toBuilder = true)
    public Student(String name, String userId, String password,String phoneNumber, String schoolName, ClassType classType, int grade, MemberType memberType, GenderType gender, Address address, AttendanceStatus attendanceStatus, Teacher teacher) {
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
        this.teacher = teacher;
    }

    //===연관관계 편의 메서드===//
    public void addParent(Parent parent) {
        parent.toBuilder().student(this);
        parentList.add(parent);
    }

    //수정 메서드
    public void changeName(String name) {
        this.name = name;
    }
    public void changeAttendanceStatus(AttendanceStatus attendanceStatus) {
        this.attendanceStatus = attendanceStatus;
    }
    public void changePhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
    public void changeSchoolName(String schoolName) {
        this.schoolName = schoolName;
    }
    public void changeUserId(String userId) {
        this.userId = userId;
    }
    public void changePassword(String password) {
        this.password = password;
    }
    public void changeGrade(int grade) {
        this.grade = grade;
    }
    public void changeGender(GenderType gender) {
        this.gender = gender;
    }
    public void changeClassType(ClassType classType) {
        this.classType = classType;
    }

    public void changeAddress(Address address) {
        this.address = address;
    }



    @Override
    public String toString() {
        return "Student{" +
                "id=" + id +
                ", userId='" + userId + '\'' +
                ", password='" + password + '\'' +
                ", name='" + name + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", schoolName='" + schoolName + '\'' +
                ", grade=" + grade +
                ", attendanceStatus=" + attendanceStatus +
                ", memberType=" + memberType +
                ", gender=" + gender +
                ", classType=" + classType +
                ", address=" + address +
                '}';
    }
}
