package com.time.studentmanage.domain.member;

import com.time.studentmanage.domain.Answer;
import com.time.studentmanage.domain.Records;
import com.time.studentmanage.domain.enums.GenderType;
import com.time.studentmanage.domain.enums.MemberType;
import com.time.studentmanage.domain.enums.Position;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
public class Teacher extends BaseMemberEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "teacher_id")
    private Long id;

    private String userId;
    private String password;
    private String name;
    private String phoneNumber;
    private String email;

    @Enumerated(EnumType.STRING)
    private Position position;

    @Enumerated(EnumType.STRING)
    private MemberType memberType;

    @Enumerated(EnumType.STRING)
    private GenderType gender;

    @OneToMany(mappedBy = "teacher", cascade = CascadeType.REMOVE)
    private List<Records> recordsList = new ArrayList<>();

    @OneToMany(mappedBy = "teacher")
    private List<Answer> answerList = new ArrayList<>();

    @OneToMany(mappedBy = "teacher")
    private List<Student> studentList = new ArrayList<>();

    public void addStudent(Student student) {
        student.setTeacher(this);
        studentList.add(student);
    }

    protected Teacher() {
    }

    public Teacher(String name, String userId, String password, String phoneNumber, MemberType memberType, Position position, String email, GenderType gender) {
        this.name = name;
        this.userId = userId;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.position = position;
        this.memberType = memberType;
        this.gender = gender;
    }
}
