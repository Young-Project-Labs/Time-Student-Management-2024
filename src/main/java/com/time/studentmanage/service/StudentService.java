package com.time.studentmanage.service;

import com.time.studentmanage.domain.enums.AttendanceStatus;
import com.time.studentmanage.domain.member.Student;
import com.time.studentmanage.domain.dto.student.StudentSaveReqDto;
import com.time.studentmanage.domain.dto.student.StudentUpdateReqDto;
import com.time.studentmanage.domain.dto.student.StudentRespDto;
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

    //학생 정보 수정
    public StudentRespDto updateStudentInfo(Long id, StudentUpdateReqDto updateReqDto) {
        Optional<Student> studentOP = studentRepository.findById(id);
        if (studentOP.isPresent()) {
            //changeEntity -> 엔티티 수정 메서드 선언함.
            studentOP.get().changeEntity(updateReqDto.getId(),updateReqDto.toEntity(bCryptPasswordEncoder));
            log.info("studentService 단 확인 ={}", studentOP.get().toString());

            Student resultStudent = studentRepository.save(studentOP.get());

            if (resultStudent.getId() != null) {
                StudentRespDto resultDto = StudentRespDto.builder().student(resultStudent).build();
                return resultDto;
            } else {
                //TODO: 예외 추가 예정(빈 객체로 리턴)
                return StudentRespDto.builder().build();
            }
        }
        //TODO: 빈 객체로 리턴 하고 예외 추가 예정
        return StudentRespDto.builder().build();

    }

    //학생 삭제
    public void deleteStudent(Long id) {
        studentRepository.deleteById(id);
    }


    //학생 재원여부 변경
    public void changeAttendanceStatus(Long id) {
        Optional<Student> studentOP = studentRepository.findById(id);
        if (studentOP.isPresent()) {
            // AttendanceStatus.Y -> AttendanceStatus.N
            if (studentOP.get().getAttendanceStatus() == AttendanceStatus.Y) {
                studentOP.get().changeAttendanceStatus(AttendanceStatus.N);
            // AttendanceStatus.N -> AttendanceStatus.Y
            } else {
                studentOP.get().changeAttendanceStatus(AttendanceStatus.Y);
            }
        }

    }

    // 학생 불러오기(메인 페이지)
    // 학교 클릭 시 학교별_학생_조회(학년별로 정렬)
    public List<StudentRespDto> getSchoolStudentList(String schoolName) {
        List<Student> studentList = studentRepository.findBySchoolNameOrderByGrade(schoolName);
        if (!studentList.isEmpty()) {
            List<StudentRespDto> studentRespDtoList = studentList.stream()
                    .map(student -> new StudentRespDto(student))
                    .collect(Collectors.toList());
            return studentRespDtoList;
        } else {
            //TODO: 예외 처리 필요
            return new ArrayList<>();
        }
    }

    //메인 페이지에서 학생 클릭 (Records -> LAZY 로딩 해서 가져와야 함.)
    public Student getStudentInfo(Long id) {
        Optional<Student> studentOP = studentRepository.findById(id);
        if (studentOP.isPresent()) {
            //TODO: Records Lazy로 가져 오는 지 확인.
            return studentOP.get();
        } else {
            //TODO: 빈 객체로 리턴 하고 예외 추가 예정
            return Student.builder()
                    .build();
        }
    }


    //TODO: 학년 클릭 시 -> 학년별_학생_조회







}
