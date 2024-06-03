package com.time.studentmanage.repository.teacher;

import com.time.studentmanage.domain.member.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TeacherRepository extends JpaRepository<Teacher, Long>, TeacherRepositoryCustom {
    Optional<Teacher> findByEmail(String email);

    Optional<Teacher> findByNameAndEmail(String name, String email);


}
