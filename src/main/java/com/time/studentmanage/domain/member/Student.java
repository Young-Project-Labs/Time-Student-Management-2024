package com.time.studentmanage.domain.member;

import com.time.studentmanage.domain.Address;
import com.time.studentmanage.domain.Record;
import com.time.studentmanage.domain.enums.AttendanceStatus;
import com.time.studentmanage.domain.enums.ClassType;
import com.time.studentmanage.domain.enums.GenderType;
import com.time.studentmanage.domain.enums.MemberType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalDateTime;
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
    private int grade;
//    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime quitDate;

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
    private List<Record> recordList = new ArrayList<>();

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "teacher_id")
    private Teacher teacher;

    @Builder(toBuilder = true)
    public Student(String name, String userId, String password, String phoneNumber, String schoolName, ClassType classType, int grade, MemberType memberType, GenderType gender, Address address, AttendanceStatus attendanceStatus, LocalDateTime quitDate, Teacher teacher) {
        this.name = name;
        this.userId = userId;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.schoolName = schoolName;
        this.grade = grade;
        this.attendanceStatus = attendanceStatus;
        this.quitDate = quitDate;
        this.memberType = memberType;
        this.gender = gender;
        this.classType = classType;
        this.address = address;
        this.teacher = teacher;
    }

    //===연관관계 편의 메서드===//
    public void addTeacher(Teacher teacher) {
        this.teacher = teacher;
        teacher.getStudentList().add(this);
    }



    //수정 메서드
    public void changeName(String name) {
        this.name = name;
    }
    public void changeAttendanceStatus(AttendanceStatus attendanceStatus) {
        this.attendanceStatus = attendanceStatus;
    }
    public void changeQuitDate(LocalDateTime quitDate) {
        this.quitDate = quitDate;
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

    public void changeEntity(Long id, Student updateStudent) {
        this.id = id;
        this.name = updateStudent.name;
        this.userId = updateStudent.userId;
        this.password = updateStudent.password;
        this.phoneNumber = updateStudent.phoneNumber;
        this.schoolName = updateStudent.schoolName;
        this.grade = updateStudent.grade;
        this.attendanceStatus = updateStudent.attendanceStatus;
        this.quitDate = updateStudent.quitDate;
        this.memberType = updateStudent.memberType;
        this.gender = updateStudent.gender;
        this.classType = updateStudent.classType;
        this.address = updateStudent.address;
        this.teacher = updateStudent.teacher;
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
                ", quitDate=" + quitDate +
                ", attendanceStatus=" + attendanceStatus +
                ", memberType=" + memberType +
                ", gender=" + gender +
                ", classType=" + classType +
                ", address=" + address +
                '}';
    }
}
