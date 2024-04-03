package com.time.studentmanage.repository;

import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.time.studentmanage.domain.enums.AttendanceStatus;
import com.time.studentmanage.domain.member.QStudent;
import com.time.studentmanage.domain.member.Student;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;

@Repository
@Transactional
public class StudentRepositoryCustomImpl implements StudentRepositoryCustom {
    private final JPAQueryFactory query;

    public StudentRepositoryCustomImpl(EntityManager em) {
        this.query = new JPAQueryFactory(em);
    }

    public List<String> findAllSchoolName() {

        QStudent student = QStudent.student;

        List<String> result = query.selectDistinct(
                        student.schoolName)
                .from(student)
                .orderBy(student.schoolName.asc())
                .fetch();

        return result;
    }

    public List<Student> findAllBySearchEngine(String schoolName, String studentName) {

        QStudent student = QStudent.student;

        List<Student> result = query.selectFrom(student)
                .where(student.attendanceStatus.eq(AttendanceStatus.Y),
                        likeSchoolName(schoolName),
                        likeStudentName(studentName))
                .fetch();

        return result;
    }

    private BooleanExpression likeStudentName(String studentName) {
        if (!StringUtils.hasText(studentName)) {
            return null;
        }

        return QStudent.student.name.like("%" + studentName + "%");
    }


    private BooleanExpression likeSchoolName(String schoolName) {

        if (!StringUtils.hasText(schoolName)) {
            return null;
        }

        return QStudent.student.schoolName.like("%" + schoolName + "%");
    }
}
