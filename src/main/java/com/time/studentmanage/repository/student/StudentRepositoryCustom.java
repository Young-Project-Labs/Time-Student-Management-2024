package com.time.studentmanage.repository.student;

import com.time.studentmanage.domain.dto.student.SelectedSchoolRespDto;
import com.time.studentmanage.domain.dto.student.StudentSearchReqDto;
import com.time.studentmanage.domain.dto.student.StudentSearchRespDto;
import com.time.studentmanage.domain.member.Student;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface StudentRepositoryCustom {

    List<String> findAllSchoolName();

    List<Student> findAllBySearchEngineWithNameNotIncludeClass(String studentName);

    Page<StudentSearchRespDto> findAllBySearchEngine(StudentSearchReqDto studentSearchReqDto, Pageable pageable);

    public Page<SelectedSchoolRespDto> findAllBySelectedSchoolName(String schoolName, String studentName, Pageable pageable);
}
