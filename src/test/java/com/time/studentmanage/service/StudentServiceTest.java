package com.time.studentmanage.service;

import com.time.studentmanage.TestUtil;
import com.time.studentmanage.domain.Address;
import com.time.studentmanage.domain.enums.AttendanceStatus;
import com.time.studentmanage.domain.enums.ClassType;
import com.time.studentmanage.domain.enums.GenderType;
import com.time.studentmanage.domain.enums.MemberType;
import com.time.studentmanage.domain.member.Student;
import com.time.studentmanage.dto.EnrollReqDto;
import com.time.studentmanage.repository.StudentRepository;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.Extensions;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;

import static com.time.studentmanage.TestUtil.createEnrollReqDto;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@Slf4j
class StudentServiceTest {
    @InjectMocks
    private StudentService studentService;

    @Mock
    private StudentRepository studentRepository;

    //패스워드 인코딩 spy로 주입
    @Spy
    private BCryptPasswordEncoder bCryptPasswordEncoder;


    @Test
    public void 학생_회원가입_테스트() {
        //given
        Long fakeId = 12L;

        EnrollReqDto enrollReqDto = createEnrollReqDto();

        Student student = enrollReqDto.toEntity(bCryptPasswordEncoder);

        ReflectionTestUtils.setField(student, "id", fakeId);

        //stub 1
        when(studentRepository.save(any())).thenReturn(student);

        //when
        Long successId = studentService.saveStudent(enrollReqDto);

        //then
        assertThat(successId).isEqualTo(fakeId);

    }


}