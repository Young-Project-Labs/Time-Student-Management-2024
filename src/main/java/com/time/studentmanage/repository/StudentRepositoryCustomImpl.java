package com.time.studentmanage.repository;

import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.time.studentmanage.domain.member.QStudent;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

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
}
