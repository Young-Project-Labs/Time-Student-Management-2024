package com.time.studentmanage.repository;

import com.time.studentmanage.domain.Answer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AnswerRepository extends JpaRepository<Answer, Long> {

    @Query("select a from Answer a join Records r on a.teacher.name = :name order by a.createDate desc")
    List<Answer> findAllByTeacherName(@Param("name") String name);
}
