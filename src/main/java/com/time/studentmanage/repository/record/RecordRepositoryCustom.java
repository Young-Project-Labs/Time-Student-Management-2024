package com.time.studentmanage.repository.record;

import com.time.studentmanage.domain.dto.record.RecordRespDto;
import com.time.studentmanage.domain.dto.record.RecordSearchReqCondition;
import com.time.studentmanage.domain.enums.SearchType;
import com.time.studentmanage.domain.member.Student;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;

public interface RecordRepositoryCustom {

    Page<RecordRespDto> findAllPaging(Student student, Pageable pageable);

    Page<RecordRespDto> findAllBySearchEngine(RecordSearchReqCondition searchCondition);
}
