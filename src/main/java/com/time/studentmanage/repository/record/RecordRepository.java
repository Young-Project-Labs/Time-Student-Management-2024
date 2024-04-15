package com.time.studentmanage.repository.record;

import com.time.studentmanage.domain.record.Record;
import com.time.studentmanage.domain.enums.RecordStatus;
import com.time.studentmanage.domain.member.Student;
import com.time.studentmanage.domain.member.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecordRepository extends JpaRepository<Record, Long>, RecordRepositoryCustom {

    @Query("select r from Record r where r.status = :recordStatus and r.student = :student order by r.createDate desc")
    List<Record> findAllByStatusAndStudent(@Param("recordStatus") RecordStatus recordStatus,
                                           @Param("student") Student student);

    @Query("select r from Record r where r.teacher = :teacher and r.status = 'PUBLISHED' order by r.createDate desc")
    List<Record> findAllByTeacher(@Param("teacher") Teacher teacher);

}
