package com.time.studentmanage.repository;

import com.time.studentmanage.domain.member.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {

    List<Student> findBySchoolName(String schoolName);
    List<Student> findBySchoolNameAndGrade(String schoolName, int grade);
}
