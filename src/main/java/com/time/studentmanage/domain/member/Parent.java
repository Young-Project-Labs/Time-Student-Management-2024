package com.time.studentmanage.domain.member;

import com.time.studentmanage.domain.Answer;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import static jakarta.persistence.FetchType.LAZY;


@Entity
@Getter @Setter
public class Parent extends Member{
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "student_id")
    private Student student;

    @OneToMany(mappedBy = "parent")
    private List<Answer> answerList = new ArrayList<>();
}
