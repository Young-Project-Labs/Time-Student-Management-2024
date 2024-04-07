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
        log.info("수정 전 teacherName={}", teacher.getName());
        log.info("수정 전 teacher={}", teacher);

        TeacherUpdateReqDto updateReqDto = updateTeacherDto();

        //when
        when(teacherRepository.findById(any())).thenReturn(Optional.of(teacher));
        teacherService.updateTeacherInfo(id, updateReqDto);

        //then

        log.info("수정 후 teacherName={}", teacher.getName());
        log.info("수정 후 teacher={}", teacher);
        Assertions.assertThat(teacher.getName()).isEqualTo(updateReqDto.getName());

    }



}