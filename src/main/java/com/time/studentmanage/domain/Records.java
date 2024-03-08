package com.time.studentmanage.domain;

import com.time.studentmanage.domain.member.Student;
import com.time.studentmanage.domain.member.Teacher;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicUpdate;

import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.FetchType.LAZY;

@Entity
@Getter
@Setter
@DynamicUpdate // 변경 감지된 필드만 update 쿼리가 날아갈 수 있도록
public class Records extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "record_id")
    private Long id;

    private String content;

    @ManyToOne(fetch = LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "teacher_id")
    private Teacher teacher;

    @ManyToOne(fetch = LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "student_id")
    private Student student;

    @OneToMany(mappedBy = "records", orphanRemoval = true)
    private List<Answer> answerList = new ArrayList<>();

    /**
     * ===연관관계 편의 메서드===
     */
    public void addTeacher(Teacher teacher) {
        this.teacher = teacher;
        teacher.getRecordsList().add(this);
    }

    public void addStudent(Student student) {
        this.student = student;
        student.getRecordsList().add(this);
    }

    /**
     * 생성자 메서드
     */
    protected Records() {
    }

    public Records(Teacher teacher, Student student, String content) {
        this.content = content;
        this.teacher = teacher;
        this.student = student;
    }
}
