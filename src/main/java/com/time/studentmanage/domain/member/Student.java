package com.time.studentmanage.domain.member;

import com.time.studentmanage.domain.Address;
import com.time.studentmanage.domain.Record;
import com.time.studentmanage.domain.enums.AttendanceStatus;
import com.time.studentmanage.domain.enums.ClassType;
import com.time.studentmanage.domain.enums.GenderType;
import com.time.studentmanage.domain.enums.MemberType;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.FetchType.LAZY;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DynamicUpdate
public class Student extends BaseMemberEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "student_id")
    private Long id;
//    @Column(unique = true) TODO: 더미데이터를 사용 하지 않을 때 주석 해제.(테스트 코드를 고쳐야 하는 번거로움 때문.)
    private String userId;
    private String password;
    private String name;
    private String phoneNumber;
    private String schoolName;
    private int grade;
    private LocalDateTime quitDate;
    private String parentName;
    private String parentPhoneNumber;

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
    private final List<Record> recordList = new ArrayList<>();

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "teacher_id")
    private Teacher teacher;

    @Builder(toBuilder = true)

    public Student(Long id, String name, String userId, String password, String phoneNumber, String schoolName, ClassType classType, int grade, MemberType memberType, GenderType gender, Address address, AttendanceStatus attendanceStatus, LocalDateTime quitDate,String parentName, String parentPhoneNumber, Teacher teacher) {
        this.id = id;
        this.name = name;
        this.userId = userId;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.schoolName = schoolName;
        this.grade = grade;
        this.attendanceStatus = attendanceStatus;
        this.quitDate = quitDate;
        this.parentName = parentName;
        this.parentPhoneNumber = parentPhoneNumber;
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
        this.name = updateStudent.getName();
        this.userId = updateStudent.getUserId();
        this.password = updateStudent.getPassword();
        this.phoneNumber = updateStudent.getPhoneNumber();
        this.schoolName = updateStudent.getSchoolName();
        this.grade = updateStudent.getGrade();
        this.attendanceStatus = updateStudent.getAttendanceStatus();
        this.quitDate = updateStudent.getQuitDate();
        this.memberType = updateStudent.getMemberType();
        this.gender = updateStudent.getGender();
        this.classType = updateStudent.getClassType();
        this.address = updateStudent.getAddress();
        this.teacher = updateStudent.getTeacher();
    }


    @Override
    public String toString() {
        return "Student{" + "id=" + id + ", userId='" + userId + '\'' + ", password='" + password + '\'' + ", name='" + name + '\'' + ", phoneNumber='" + phoneNumber + '\'' + ", schoolName='" + schoolName + '\'' + ", grade=" + grade + ", quitDate=" + quitDate + ", attendanceStatus=" + attendanceStatus + ", memberType=" + memberType + ", gender=" + gender + ", classType=" + classType + ", address=" + address + '}';
    }
}
