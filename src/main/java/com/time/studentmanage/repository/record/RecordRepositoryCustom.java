package com.time.studentmanage.repository.record;

import com.time.studentmanage.domain.dto.record.RecordRespDto;
import com.time.studentmanage.domain.member.Student;
import com.time.studentmanage.domain.record.Record;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;

public interface RecordRepositoryCustom {

    Page<RecordRespDto> findAllByContentSearch(Student student, String searchWords,
                                        LocalDateTime fromDate, LocalDateTime toDate, Pageable pageable);

    Page<RecordRespDto> findAllByTeacherNameSearch(Student student, String searchWords,
                                            LocalDateTime fromDate, LocalDateTime toDate, Pageable pageable);

    Page<RecordRespDto> findAllPaging(Student student, Pageable pageable);
}
