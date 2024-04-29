package com.time.studentmanage.repository.student;

import com.time.studentmanage.domain.member.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long>, StudentRepositoryCustom {

    List<Student> findBySchoolNameOrderByGrade(String schoolName);

    List<Student> findBySchoolNameAndGrade(String schoolName, int grade);

    Optional<Student> findByNameAndPhoneNumber(String name, String phoneNumber);

    Optional<Student> findByUserId(String userId);

    Boolean existsByUserId(String checkId);

    List<Student> findAllBySchoolNameOrderByGradeAsc(String schoolName);

    Optional<Student> findByUserIdAndProvider(String email, String provider);

    Optional<Student> findByNameAndEmail(String name, String email);

    Optional<Student> findByEmail(String email);

    Optional<Student> findByUserIdAndEmail(String userId, String toEmail);
}
