package com.time.studentmanage.domain.member;

import com.time.studentmanage.domain.Answer;
import com.time.studentmanage.domain.enums.GenderType;
import com.time.studentmanage.domain.enums.MemberType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static jakarta.persistence.FetchType.LAZY;


@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DynamicUpdate
public class Parent extends BaseMemberEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "parent_id")
    private Long id;

    private String name;
    private String phoneNumber;

    @Enumerated(EnumType.STRING)
    private MemberType memberType;

    @Enumerated(EnumType.STRING)
    private GenderType gender;

    @ManyToOne(fetch = LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "student_id")
    private Student student;

    @OneToMany(mappedBy = "parent")
    private List<Answer> answerList = new ArrayList<>();


    @Builder
    public Parent(String name, String phoneNumber, MemberType memberType, GenderType gender, Student student) {
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.memberType = memberType;
        this.gender = gender;
        this.student = student;
    }

    //===연관관계 편의 메서드===//
    public void addStudent(Student student) {
        this.student = student;
        student.getParentList().add(this);
    }

    //=== 수정 메서드===//
    public void changeName(String name) {
        this.name = name;
    }

    public void changePhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void changeGender(GenderType gender) {
        this.gender = gender;
    }
}
