package com.time.studentmanage.repository.teacher;

import com.time.studentmanage.domain.dto.teacher.TeacherRespDto;
import com.time.studentmanage.domain.dto.teacher.TeacherSearchReqDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
public interface TeacherRepositoryCustom {
    Page<TeacherRespDto> findAllPaging(Pageable pageable);

    Page<TeacherRespDto> findSearchDtoPaging(TeacherSearchReqDto searchReqDto, Pageable pageable);
}
