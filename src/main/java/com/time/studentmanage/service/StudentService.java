package com.time.studentmanage.service;

import com.time.studentmanage.domain.member.Student;
import com.time.studentmanage.domain.dto.student.StudentSaveReqDto;
import com.time.studentmanage.domain.dto.student.StudentUpdateReqDto;
import com.time.studentmanage.domain.dto.student.StudentRespDto;
import com.time.studentmanage.exception.DataNotFoundException;
import com.time.studentmanage.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class StudentService {
    private final StudentRepository studentRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    //학생 회원가입
    public Long saveStudent(StudentSaveReqDto saveReqDto) {
        //1. 학생 존재 여부 확인(학생 & 전화번호)
        Optional<Student> studentOP = studentRepository.findByNameAndPhoneNumber(saveReqDto.getName(), saveReqDto.getPhoneNumber());

        if (studentOP.isPresent()) {
            throw new IllegalArgumentException("이미 존재 하는 학생 입니다.");
        }
        //2. 패스워드 인코딩
        saveReqDto.setPassword(bCryptPasswordEncoder.encode(saveReqDto.getPassword()));

        //3. save로 저장
        Student result = studentRepository.save(saveReqDto.toEntity(bCryptPasswordEncoder));
        return result.getId();
    }

    //학생_정보_수정
    public StudentRespDto updateStudentInfo(Long id, StudentUpdateReqDto updateReqDto) {
        Optional<Student> studentOP = studentRepository.findById(id);

        if (!studentOP.isPresent()) {
            throw new DataNotFoundException("존재하지 않는 ID입니다.");
        }
        //changeEntity
        studentOP.get().changeEntity(updateReqDto.getId(),updateReqDto.toEntity());

        Student resultStudent = studentRepository.save(studentOP.get());
        //dto 변환
        StudentRespDto resultDto = StudentRespDto.builder().student(resultStudent).build();
        return resultDto;

    }

    //학생_삭제
    public void deleteStudent(Long id) {
        studentRepository.deleteById(id);
    }


    // 학교별_학생_조회(학년별_정렬)
    @Transactional(readOnly = true)
    public List<StudentRespDto> getSchoolStudentList(String schoolName) {
        List<Student> studentList = studentRepository.findBySchoolNameOrderByGrade(schoolName);
        if (studentList.isEmpty()) {
            return new ArrayList<>();
        }

        List<StudentRespDto> studentRespDtoList = studentList.stream()
                .map(student -> new StudentRespDto(student))
                .collect(Collectors.toList());
        return studentRespDtoList;

    }
    @Transactional(readOnly = true)
    // 학생_상세_정보_조회
    public StudentRespDto getStudentInfo(Long id) {
        Optional<Student> studentOP = studentRepository.findById(id);
        if (!studentOP.isPresent()) {
            throw new DataNotFoundException("해당하는 학생이 없습니다.");
        }

        StudentRespDto respDto = StudentRespDto.builder().student(studentOP.get()).build();
        return respDto;

    }


    //TODO: 학년 클릭 시 -> 학년별_학생_조회







}
