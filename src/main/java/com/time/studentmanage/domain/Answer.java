package com.time.studentmanage.domain;

import com.time.studentmanage.domain.enums.AnswerStatus;
import com.time.studentmanage.domain.member.Parent;
import com.time.studentmanage.domain.member.Teacher;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import static jakarta.persistence.FetchType.LAZY;

@Entity
@Getter
@Setter
public class Answer extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "answer_id")
    private Long id;

    private String content;

    @Enumerated(EnumType.STRING)
    private AnswerStatus status; // 비밀 댓글 유무: GENERAL, SECRET

    @OneToOne(fetch = LAZY)
    private Answer parentAnswer; // 부모 댓글

    @ManyToOne(fetch = LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "record_id")
    private Records records;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "teacher_id")
    private Teacher teacher;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "parent_id")
    private Parent parent;

    /**
     * ===연관관계 편의 메서드===
     */
    public void addRecords(Records records) {
        this.records = records;
        records.getAnswerList().add(this);
    }

    public void addTeacher(Teacher teacher) {
        this.teacher = teacher;
        teacher.getAnswerList().add(this);
    }

    public void addParent(Parent parent) {
        this.parent = parent;
        parent.getAnswerList().add(this);
    }

    /**
     * 생성자 메서드
     */
    protected Answer() {
    }

    public Answer(Records records, String content, AnswerStatus status) {
        this.content = content;
        this.status = status;
        this.records = records;
    }
}
