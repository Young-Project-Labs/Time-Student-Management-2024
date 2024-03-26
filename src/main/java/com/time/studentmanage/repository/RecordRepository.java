package com.time.studentmanage.repository;

import com.time.studentmanage.domain.Record;
import com.time.studentmanage.domain.enums.RecordStatus;
import com.time.studentmanage.domain.member.Student;
import com.time.studentmanage.domain.member.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface RecordRepository extends JpaRepository<Record, Long> {

    @Query("select r from Record r where r.status = :recordStatus and r.student = :student order by r.createDate desc")
    List<Record> findAllByStatusAndStudent(@Param("recordStatus") RecordStatus recordStatus,
                                           @Param("student") Student student);

    @Query("select r from Record r where r.teacher = :teacher and r.status = 'PUBLISHED' order by r.createDate desc")
    List<Record> findAllByTeacher(@Param("teacher") Teacher teacher);

    @Query("select r from Record r where 1=1 and r.status = :status and r.student = :student and r.content like :content")
    List<Record> findAllByContent(@Param("status") RecordStatus recordStatus,
                                  @Param("student") Student student,
                                  @Param("content") String content);

    @Query("select r from Record r where 1=1 and r.status = :status and r.student = :student and r.teacher.name = :name")
    List<Record> findAllByTeacherName(@Param("status") RecordStatus recordStatus,
                                      @Param("student") Student student,
                                      @Param("name") String name);

    @Query("select r from Record r where 1=1 and r.status = :status and r.student = :student and ('date_format', r.createDate, '%Y-%m-%d') = function('date_format', :from, '%Y-%m-%d')")
    List<Record> findAllBySelectedDateRange(@Param("status") RecordStatus recordStatus,
                                            @Param("student") Student student,
                                            @Param("from") LocalDateTime fromDate);

}
