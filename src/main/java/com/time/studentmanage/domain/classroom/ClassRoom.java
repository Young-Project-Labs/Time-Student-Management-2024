package com.time.studentmanage.domain.classroom;

import com.time.studentmanage.domain.record.BaseTimeEntity;
import com.time.studentmanage.domain.enums.ClassStatus;
import com.time.studentmanage.domain.enums.ClassType;
import com.time.studentmanage.domain.member.Student;
import com.time.studentmanage.domain.member.Teacher;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;

import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.FetchType.*;
import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
@DynamicUpdate
public class ClassRoom extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "class_room_id")
    private Long id;

    private String name; // 학급 대표 이름

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "teacher_id")
    private Teacher teacher; // 학급 담당 선생님

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "student_id")
    private Student student;

    @OneToMany(mappedBy = "classRoom", cascade = CascadeType.REMOVE) // 삭제 주기를 같게 하기 위함
    private List<ClassRoomDate> classRoomDateList = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    private ClassType classType;

    @Enumerated(EnumType.STRING)
    private ClassStatus classStatus;

    @Builder
    public ClassRoom(String name, Teacher teacher, Student student, ClassType classType, ClassStatus classStatus) {
        this.name = name;
        this.teacher = teacher;
        this.student = student;
        this.classType = classType;
        this.classStatus = classStatus;
    }
}
