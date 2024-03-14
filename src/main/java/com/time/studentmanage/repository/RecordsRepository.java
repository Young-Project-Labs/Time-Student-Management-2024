package com.time.studentmanage.repository;

import com.time.studentmanage.domain.Records;
import com.time.studentmanage.domain.enums.RecordStatus;
import com.time.studentmanage.domain.member.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecordsRepository extends JpaRepository<Records, Long> {
    @Query("select r from Records r where r.student.name = :name and r.student.schoolName like :schoolName")
    List<Records> findAllByStudentNameWithSchoolName(@Param("name") String name, @Param("schoolName") String schoolName);

    List<Records> findAllByStatusAndStudent(RecordStatus recordStatus, Student student);
}
