package com.time.studentmanage.repository.student;

import com.querydsl.core.Tuple;
import com.time.studentmanage.domain.member.Student;

import java.util.List;

public interface StudentRepositoryCustom {

    List<String> findAllSchoolName();

    List<Student> findAllBySearchEngine(String schoolName, String studentName);
}
