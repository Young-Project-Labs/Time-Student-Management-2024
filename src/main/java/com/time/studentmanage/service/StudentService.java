package com.time.studentmanage.service;

import com.time.studentmanage.domain.dto.student.*;
import com.time.studentmanage.domain.member.Student;
import com.time.studentmanage.exception.DataNotFoundException;
import com.time.studentmanage.repository.student.StudentRepository;
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

    // 학생 회원가입
    public Long saveStudent(StudentSaveReqDto saveReqDto) {
        // 1. 학생 존재 여부 확인(학생 & 전화번호)
        Optional<Student> studentOP = studentRepository.findByNameAndPhoneNumber(saveReqDto.getName(),
                saveReqDto.getPhoneNumber());

        if (studentOP.isPresent()) {
            throw new IllegalArgumentException("이미 존재 하는 학생 입니다.");
        }

        // 2. save로 저장(toEntity 시 패스워드 인코딩 진행)
        Student result = studentRepository.save(saveReqDto.toEntity(bCryptPasswordEncoder));
        return result.getId();
    }

    // 아이디 중복 체크
    @Transactional(readOnly = true)

    public Boolean checkIdDuplication(String checkId) {
        return studentRepository.existsByUserId(checkId);
    }

    // 학생_정보_수정
    public StudentRespDto updateStudentInfo(Long id, StudentUpdateReqDto updateReqDto) {
        Optional<Student> studentOP = studentRepository.findById(id);

        if (!studentOP.isPresent()) {
            throw new DataNotFoundException("존재하지 않는 ID입니다.");
        }

        Student updateStudent = updateReqDto.toEntity();
        log.info("service check={}", updateStudent);
        Student resultStudent = studentRepository.save(updateStudent);
        // dto 변환
        StudentRespDto resultDto = new StudentRespDto(resultStudent);
        return resultDto;

    }

    // 학생_삭제
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

    // 학생_상세_정보_조회
    @Transactional(readOnly = true)
    public StudentRespDto getStudentInfo(Long id) {
        Optional<Student> studentOP = studentRepository.findById(id);
        if (!studentOP.isPresent()) {
            throw new DataNotFoundException("해당하는 학생이 없습니다.");
        }

        StudentRespDto respDto = new StudentRespDto(studentOP.get());
        return respDto;

    }

    @Transactional(readOnly = true)
    public StudentSchoolListRespDto getAllSchoolName() {
        List<String> allSchoolName = studentRepository.findAllSchoolName();

        StudentSchoolListRespDto schoolRespDto = new StudentSchoolListRespDto();

        for (String name : allSchoolName) {
            if (name.contains("초등학교")) {
                schoolRespDto.getElementarySchools().add(name);
            } else if (name.contains("중학교")) {
                schoolRespDto.getMiddleSchools().add(name);
            } else if (name.contains("고등학교")) {
                schoolRespDto.getHighSchools().add(name);
            }
        }

        return schoolRespDto;
    }

    @Transactional(readOnly = true)
    public List<SelectedSchoolRespDto> getAllStudentsBySchoolName(String schoolName) {

        List<Student> studentList = studentRepository.findAllBySchoolNameOrderByGradeAsc(schoolName);
        List<SelectedSchoolRespDto> resultDto = studentList.stream()
                .map(s -> new SelectedSchoolRespDto(s.getId(), s.getName(), s.getGrade()))
                .collect(Collectors.toList());

        if (resultDto == null || resultDto.size() == 0) {
            throw new DataNotFoundException("검색 결과가 존재하지 않습니다.");
        }

        return resultDto;
    }

    @Transactional(readOnly = true)
    public List<SearchStudentRespDto> getSearchedStudent(String content) {

        if (content.contains("+")) {
            String[] contentBits = content.split("\\+");

            String schoolName = contentBits[0].trim();
            String studentName = contentBits[1].trim();

            List<Student> searchedBySchoolNameAndStudentNameList = studentRepository.findAllBySearchEngine(schoolName,
                    studentName);

            List<SearchStudentRespDto> resultDto = createRespDtoList(searchedBySchoolNameAndStudentNameList);

            if (resultDto == null || resultDto.size() == 0) {
                throw new DataNotFoundException("검색 결과가 존재하지 않습니다.");
            }

            return resultDto;
        }

        List<Student> searchedByStudentNameList = studentRepository.findAllBySearchEngine(null, content.trim());
        List<SearchStudentRespDto> resultDto = createRespDtoList(searchedByStudentNameList);

        if (resultDto == null || resultDto.size() == 0) {
            throw new DataNotFoundException("검색 결과가 존재하지 않습니다.");
        }

        return resultDto;
    }

    private List<SearchStudentRespDto> createRespDtoList(List<Student> targetList) {
        List<SearchStudentRespDto> resultDto = targetList.stream()
                .map(s -> new SearchStudentRespDto(s.getId(), s.getName(), s.getSchoolName(), s.getGrade()))
                .collect(Collectors.toList());
        return resultDto;
    }
}
