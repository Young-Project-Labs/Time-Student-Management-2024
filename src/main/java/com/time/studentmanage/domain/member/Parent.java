package com.time.studentmanage.domain.member;

import com.time.studentmanage.domain.Answer;
import com.time.studentmanage.domain.enums.GenderType;
import com.time.studentmanage.domain.enums.MemberType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicUpdate;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static jakarta.persistence.FetchType.LAZY;


@Entity
@Getter
@Setter
@DynamicUpdate
public class Parent extends BaseMemberEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "parent_id")
    private Long id;

    private String name;
    private String phone_number;

    @Enumerated(EnumType.STRING)
    private MemberType memberType;

    @Enumerated(EnumType.STRING)
    private GenderType gender;

    @ManyToOne(fetch = LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "student_id")
    private Student student;

    @OneToMany(mappedBy = "parent")
    private List<Answer> answerList = new ArrayList<>();

    protected Parent() {
    }

    public Parent(String name, String phone_number, MemberType memberType, GenderType gender) {
        this.name = name;
        this.phone_number = phone_number;
        this.memberType = memberType;
        this.gender = gender;
    }

    //===연관관계 편의 메서드===//
    public void addStudent(Student student) {
        this.student = student;
        student.getParentList().add(this);
    }
}
