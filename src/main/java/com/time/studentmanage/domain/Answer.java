package com.time.studentmanage.domain;

import com.time.studentmanage.domain.enums.AnswerStatus;
import com.time.studentmanage.domain.member.Parent;
import com.time.studentmanage.domain.member.Teacher;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;

import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.FetchType.LAZY;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DynamicUpdate
public class Answer extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "answer_id")
    private Long id;

    private String content;

    @Enumerated(EnumType.STRING)
    private AnswerStatus status; // GENERAL, SECRET, DELETED : 일반 댓글, 비밀 댓글, 삭제된 댓글

    @ManyToOne(fetch = LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "parent_answer_id")
    private Answer parentAnswer; // 부모 댓글

    @OneToMany(mappedBy = "parentAnswer")
    private List<Answer> childAnswerList = new ArrayList<>();

    @ManyToOne(fetch = LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "record_id")
    private Record record;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "teacher_id")
    private Teacher teacher;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "parent_id")
    private Parent parent;

    /**
     * 생성자 메서드
     */
    @Builder
    public Answer(Record record, Teacher teacher, String content, AnswerStatus status) {
        this.record = record;
        this.teacher = teacher;
        this.content = content;
        this.status = status;
    }

    /**
     * ===연관관계 편의 메서드===
     */
    public void addRecord(Record record) {
        this.record = record;
        record.getAnswerList().add(this);
    }

    public void addTeacher(Teacher teacher) {
        this.teacher = teacher;
        teacher.getAnswerList().add(this);
    }

    public void addParent(Parent parent) {
        this.parent = parent;
        parent.getAnswerList().add(this);
    }

    public void addParentAnswer(Answer answer) {
        this.parentAnswer = answer;
        parentAnswer.getChildAnswerList().add(this);
    }

    public void deleteAnswer() {
        this.status = AnswerStatus.DELETED;
    }

    public void changeAnswer(String content) {
        this.content = content;
    }
}
