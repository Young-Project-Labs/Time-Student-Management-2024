package com.time.studentmanage.domain.member;

import com.time.studentmanage.domain.record.Record;
import com.time.studentmanage.domain.enums.GenderType;
import com.time.studentmanage.domain.enums.MemberType;
import com.time.studentmanage.domain.enums.Position;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@ToString
@NoArgsConstructor(access = PROTECTED)
public class Teacher extends BaseMemberEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "teacher_id")
    private Long id;
    private String password;
    private String name;
    private String phoneNumber;
//    @Column(unique = true) TODO: 더미데이터를 사용 하지 않을 때 주석 해제.(테스트 코드를 고쳐야 하는 번거로움 때문.)
    private String email;

    @Enumerated(EnumType.STRING)
    private Position position;

    @Enumerated(EnumType.STRING)
    private MemberType memberType;

    @Enumerated(EnumType.STRING)
    private GenderType gender;

    @OneToMany(mappedBy = "teacher", cascade = CascadeType.REMOVE)
    private final List<Record> recordList = new ArrayList<>();

    @OneToMany(mappedBy = "teacher")
    private final List<Student> studentList = new ArrayList<>();


    @Builder
    public Teacher(String name, String password, String phoneNumber, MemberType memberType, Position position, String email, GenderType gender) {
        this.name = name;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.position = position;
        this.memberType = memberType;
        this.gender = gender;
    }

    public void changeEntity(Long id, Teacher updateTeacher) {
        this.id = id;
        this.name = updateTeacher.getName();
        this.phoneNumber = updateTeacher.getPhoneNumber();
        this.email = updateTeacher.getEmail();
        this.position = updateTeacher.getPosition();
        this.memberType = updateTeacher.getMemberType();
        this.gender = updateTeacher.getGender();
    }
    //패스워드만 변경
    public void setPassword(String password) {
        this.password = password;
    }
}
