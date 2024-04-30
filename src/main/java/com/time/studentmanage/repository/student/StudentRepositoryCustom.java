package com.time.studentmanage.repository.student;

import com.querydsl.core.Tuple;
import com.time.studentmanage.domain.dto.student.StudentSearchReqDto;
import com.time.studentmanage.domain.dto.student.StudentSearchRespDto;
import com.time.studentmanage.domain.member.Student;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface StudentRepositoryCustom {

    List<String> findAllSchoolName();

    List<Student> findAllBySearchEngine(String schoolName, String studentName);

    List<Student> findAllBySearch(String searchType, String keyword);

    List<Student> findAllBySearchEngineWithNameNotIncludeClass(String studentName);

    Page<StudentSearchRespDto> findAllBySearchEngine(StudentSearchReqDto studentSearchReqDto, Pageable pageable);
}
