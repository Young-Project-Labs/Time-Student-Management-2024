package com.time.studentmanage.service;

import com.time.studentmanage.domain.member.Student;
import com.time.studentmanage.dto.StudentSaveReqDto;
import com.time.studentmanage.dto.StudentUpdateReqDto;
import com.time.studentmanage.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class StudentService {
    private final StudentRepository studentRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    //학생 회원가입
    @Transactional
    public Long saveStudent(StudentSaveReqDto saveReqDto) {
        //1. 학생 존재 여부 확인(학생 & 부모)

        //2. 패스워드 인코딩
        saveReqDto.setPassword(bCryptPasswordEncoder.encode(saveReqDto.getPassword()));

        //3. save로 저장
        Student result = studentRepository.save(saveReqDto.toEntity(bCryptPasswordEncoder));
        return result.getId();
    }
    @Transactional
    //학생 정보 수정
    public Student updateStudent(Long id, StudentUpdateReqDto updateReqDto) {
        Optional<Student> studentOP = studentRepository.findById(id);
        if (studentOP.isPresent()) {
            //changeEntity -> 엔티티 수정 메서드 선언함.
            studentOP.get().changeEntity(updateReqDto.getId(),updateReqDto.toEntity(bCryptPasswordEncoder));
            log.info("studentService 단 확인 ={}", studentOP.get().toString());
            //save -> id 값이 있으면 INSERT, id 값이 없으면 UPDATE 쿼리를 날린다.
            Student resultStudent = studentRepository.save(studentOP.get());
            if (resultStudent.getId() != null) {
                return resultStudent;
            } else {
                //TODO: 예외 추가 예정(빈 객체로 리턴)
                return Student.builder()
                        .build();
            }
        }
        //TODO: 빈 객체로 리턴 하고 예외 추가 예정
        return Student.builder()
                .build();

    }

    //학생 삭제
    public void deleteStudent() {

    }

    //학생 불러오기(메인 페이지)
    //- 학교명 불러오기 -> 학교 클릭 시 학교별_학생_조회_ / 학년 클릭 시 -> 학년별_학생_조회
    public void findStudents() {

    }







}
