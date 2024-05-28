package com.time.studentmanage.domain.record;

import com.time.studentmanage.domain.enums.RecordStatus;
import com.time.studentmanage.domain.member.Student;
import com.time.studentmanage.domain.member.Teacher;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;

import static jakarta.persistence.FetchType.LAZY;
import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
@DynamicUpdate // 변경 감지된 필드만 update 쿼리가 날아갈 수 있도록
public class Record extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "record_id")
    private Long id;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String content;

    @ManyToOne(fetch = LAZY) //, cascade = CascadeType.PERSIST
    @JoinColumn(name = "teacher_id")
    private Teacher teacher;

    @ManyToOne(fetch = LAZY) //, cascade = CascadeType.PERSIST
    @JoinColumn(name = "student_id")
    private Student student;

    @Enumerated(EnumType.STRING)
    private RecordStatus status;

    /**
     * 생성자 메서드
     */
    @Builder
    public Record(String content, Teacher teacher, Student student, RecordStatus status) {
        this.content = content;
        this.teacher = teacher;
        this.student = student;
        this.status = status;
    }

    /**
     * ===연관관계 편의 메서드===
     */
    public void addTeacher(Teacher teacher) {
        this.teacher = teacher;
        teacher.getRecordList().add(this);
    }

    public void addStudent(Student student) {
        this.student = student;
        student.getRecordList().add(this);
    }

    public void changeContent(String content) {
        this.content = content;
    }

    public void changeRecordStatus(RecordStatus status) {
        this.status = status;
    }
}
