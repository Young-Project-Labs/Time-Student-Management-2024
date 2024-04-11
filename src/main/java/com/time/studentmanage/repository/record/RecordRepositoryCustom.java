package com.time.studentmanage.repository.record;

import com.time.studentmanage.domain.record.Record;
import com.time.studentmanage.domain.member.Student;

import java.time.LocalDateTime;
import java.util.List;

public interface RecordRepositoryCustom {

    List<Record> findAllByContentSearch(Student student, String searchWords,
                                        LocalDateTime fromDate, LocalDateTime toDate);

    List<Record> findAllByTeacherNameSearch(Student student, String searchWords,
                                            LocalDateTime fromDate, LocalDateTime toDate);

}
