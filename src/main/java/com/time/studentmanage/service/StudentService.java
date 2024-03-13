package com.time.studentmanage.service;

import com.time.studentmanage.domain.Address;
import com.time.studentmanage.domain.enums.AttendanceStatus;
import com.time.studentmanage.domain.enums.ClassType;
import com.time.studentmanage.domain.enums.GenderType;
import com.time.studentmanage.domain.enums.MemberType;
import com.time.studentmanage.domain.member.Student;
import com.time.studentmanage.dto.EnrollReqDto;
import com.time.studentmanage.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class StudentService {
    private final StudentRepository studentRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    //학생 회원가입
    @Transactional
    public Long saveStudent(EnrollReqDto enrollReqDto) {
        //1. 학생 존재 여부 확인(학생 & 부모)

        //2. 패스워드 인코딩
        enrollReqDto.setPassword(bCryptPasswordEncoder.encode(enrollReqDto.getPassword()));

        //3. save로 저장
        Student result = studentRepository.save(enrollReqDto.toEntity(bCryptPasswordEncoder));
        return result.getId();
    }

    //학생 정보 수정
    public void updateStudentInfo(EnrollReqDto enrollReqDto) {
        //0. 권한이 선생 or 본인 확인

        //1. 학생 존재 여부 확인

        //2. save로 저장

    }

    //학생 삭제
    public void deleteStudent() {

    }

    //학생 불러오기(메인 페이지)
    //- 학교명 불러오기 -> 학교 클릭 시 학교별_학생_조회_ / 학년 클릭 시 -> 학년별_학생_조회
    public void findStudents() {

    }







}
