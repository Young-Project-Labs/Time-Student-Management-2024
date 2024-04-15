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

    private String classInfo; // 학급 특이사항

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "teacher_id")
    private Teacher teacher; // 학급 담당 선생님

    @OneToMany(mappedBy = "classRoom")
    private List<Student> studentList = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    private ClassType classType;

    @Builder
    public ClassRoom(String name, String classInfo, Teacher teacher, ClassType classType) {
        this.name = name;
        this.classInfo = classInfo;
        this.teacher = teacher;
        this.classType = classType;
    }

    public void changeClassRoomName(String name) {
        this.name = name;
    }

    public void changeClassInfo(String classInfo) {
        this.classInfo = classInfo;
    }

    public void changeClassType(ClassType classType) {
        this.classType = classType;
    }

    public void removeStudentList(Student student) {
        studentList.remove(student);
    }

}
