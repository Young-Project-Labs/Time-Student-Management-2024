package com.time.studentmanage.service;

import com.time.studentmanage.TestUtil;
import com.time.studentmanage.domain.dto.teacher.TeacherRespDto;
import com.time.studentmanage.domain.dto.teacher.TeacherUpdateReqDto;
import com.time.studentmanage.domain.member.Teacher;
import com.time.studentmanage.repository.TeacherRepository;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;

import static com.time.studentmanage.TestUtil.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@Slf4j
class TeacherServiceTest {
    @InjectMocks
    private TeacherService teacherService;
    @Mock
    private TeacherRepository teacherRepository;

    @Test
    void 선생_정보_수정_테스트(){
        //given
        Long id = 1L;
        Teacher teacher = createTeacher();
        ReflectionTestUtils.setField(teacher,"id",id);

        TeacherUpdateReqDto updateReqDto = updateTeacherDto();
        Teacher updateTeacher = updateReqDto.toEntity();
        ReflectionTestUtils.setField(updateTeacher,"id",id);

        //when
        when(teacherRepository.findById(any())).thenReturn(Optional.of(teacher));
        when(teacherRepository.save(any())).thenReturn(updateTeacher);

        //then
        TeacherRespDto teacherRespDto = teacherService.updateTeacherInfo(id, updateReqDto);
        Assertions.assertThat(teacherRespDto.getName()).isEqualTo(updateReqDto.getName());


    }



}