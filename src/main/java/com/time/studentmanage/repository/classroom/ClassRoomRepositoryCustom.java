package com.time.studentmanage.repository.classroom;

import com.time.studentmanage.domain.dto.classroom.ClassRoomRespDto;
import com.time.studentmanage.domain.dto.classroom.ClassRoomSearchReqDto;
import com.time.studentmanage.domain.member.Teacher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ClassRoomRepositoryCustom {
    Page<ClassRoomRespDto> findAllPaging(Teacher teacher, Pageable pageable);

    Page<ClassRoomRespDto> findAllBySearchEngine(ClassRoomSearchReqDto classRoomSearchReqDto, Pageable pageable);
}
