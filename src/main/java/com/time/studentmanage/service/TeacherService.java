package com.time.studentmanage.service;

import com.time.studentmanage.domain.dto.teacher.TeacherRespDto;
import com.time.studentmanage.domain.dto.teacher.TeacherUpdateReqDto;
import com.time.studentmanage.domain.member.Teacher;
import com.time.studentmanage.exception.DataNotFoundException;
import com.time.studentmanage.repository.TeacherRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class TeacherService {
    private final TeacherRepository teacherRepository;
    // 선생_정보_수정
    public TeacherRespDto updateTeacherInfo(Long id, TeacherUpdateReqDto updateReqDto) {
        Optional<Teacher> teacherOP = teacherRepository.findById(id);
        if (!teacherOP.isPresent()) {
            throw new DataNotFoundException("존재하지 않는 ID입니다.");
        }

        teacherOP.get().changeEntity(updateReqDto.getId(), updateReqDto.toEntity());

        Teacher resultTeacher = teacherRepository.save(teacherOP.get());

        TeacherRespDto teacherRespDto = TeacherRespDto.builder().teacher(resultTeacher).build();
        return teacherRespDto;
    }




}
