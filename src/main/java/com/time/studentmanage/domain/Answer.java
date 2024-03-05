package com.time.studentmanage.domain;

import com.time.studentmanage.domain.enums.AnswerStatus;
import com.time.studentmanage.domain.member.Parent;
import com.time.studentmanage.domain.member.Teacher;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

import static jakarta.persistence.FetchType.LAZY;

@Entity
@Getter
@Setter
public class Answer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "answer_id")
    private Long id;

    private String content;
    private LocalDateTime createDate;

    @OneToOne(fetch = LAZY)
    private Answer parentAnswer; // 부모 댓글

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "record_id")
    private Records records;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "teacher_id")
    private Teacher teacher;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "parent_id")
    private Parent parent;

    @Enumerated(EnumType.STRING)
    private AnswerStatus status; // 비밀 댓글 유무: GENERAL, SECRET


    //===연관관계 편의 메서드===//
    public void addRecords(Records records) {
        this.records = records;
        records.getAnswerList().add(this);
    }
}
