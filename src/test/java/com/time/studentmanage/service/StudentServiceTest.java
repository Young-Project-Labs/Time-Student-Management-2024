package com.time.studentmanage.service;

import com.time.studentmanage.domain.Records;
import com.time.studentmanage.domain.member.Student;
import com.time.studentmanage.domain.member.Teacher;
import com.time.studentmanage.dto.StudentSaveReqDto;
import com.time.studentmanage.dto.StudentUpdateReqDto;
import com.time.studentmanage.dto.StudentRespDto;
import com.time.studentmanage.repository.StudentRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.time.studentmanage.TestUtil.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@Slf4j
class StudentServiceTest {
    @InjectMocks
    private StudentService studentService;

    @Mock
    private StudentRepository studentRepository;

    @Spy
    private BCryptPasswordEncoder bCryptPasswordEncoder;


    @Test
    public void 학생_회원가입_테스트() {
        //given
        Long fakeId = 12L;

        StudentSaveReqDto enrollReqDto = createStudentDto();

        Student student = enrollReqDto.toEntity(bCryptPasswordEncoder);

        ReflectionTestUtils.setField(student, "id", fakeId);

        //stub 1
        when(studentRepository.save(any())).thenReturn(student);

        //when
        Long successId = studentService.saveStudent(enrollReqDto);

        //then
        assertThat(successId).isEqualTo(fakeId);

    }

    @Test
    void 학생_수정_테스트(){
        //given
        Long fakeId = 1L;
        //수정 전 findById로 찾은 엔티티
        StudentSaveReqDto saveReqDto = createStudentDto();
        Student student = saveReqDto.toEntity(bCryptPasswordEncoder);
        ReflectionTestUtils.setField(student, "id", fakeId);

        //수정 후 예상 엔티티
        StudentUpdateReqDto updateReqDto = updateStudentDto();
        Student updateStudent = updateReqDto.toEntity(bCryptPasswordEncoder);
        ReflectionTestUtils.setField(updateStudent, "id", fakeId);

        //stub
        when(studentRepository.findById(any())).thenReturn(Optional.of(student));
        //stub2
        when(studentRepository.save(any())).thenReturn(updateStudent);

        //when
        StudentRespDto respDto = studentService.updateStudentInfo(fakeId, updateReqDto);
        log.info("respDto={}",respDto);

        //then
        assertThat(respDto.getName()).isEqualTo(updateStudent.getName());


    }
    @Test
    void 학생_학교별_조회_테스트(){
        //given
        Long fakeId = 1L;

        List<Student> studentList = createManyStudent();
        String schoolName = "용호초등학교";
        List<Student> resultStudent = studentList.stream()
                .filter(student -> student.getSchoolName().equals(schoolName))
                .collect(Collectors.toList());

        //stub
        when(studentRepository.findBySchoolNameOrderByGrade(any())).thenReturn(resultStudent);

        //when
        List<StudentRespDto> studentRespDtoList = studentService.getSchoolStudentList(schoolName);
        //then
        assertThat(studentRespDtoList.size()).isEqualTo(resultStudent.size());

    }

}