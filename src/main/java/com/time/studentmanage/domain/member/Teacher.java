package com.time.studentmanage.domain.member;

import com.time.studentmanage.domain.Answer;
import com.time.studentmanage.domain.Records;
import com.time.studentmanage.domain.enums.Position;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
public class Teacher extends Member{
    @Enumerated(EnumType.STRING)
    private Position position;
    private String email;

    @OneToMany(mappedBy = "teacher")
    private List<Records> recordsList = new ArrayList<>();

    @OneToMany(mappedBy = "teacher")
    private List<Answer> answerList = new ArrayList<>();

}
