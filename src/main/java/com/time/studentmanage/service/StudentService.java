package com.time.studentmanage.service;

import com.time.studentmanage.domain.dto.student.StudentRespDto;
import com.time.studentmanage.domain.dto.student.StudentSaveReqDto;
import com.time.studentmanage.domain.dto.student.StudentSchoolListRespDto;
import com.time.studentmanage.domain.dto.student.StudentUpdateReqDto;
import com.time.studentmanage.domain.member.Student;
import com.time.studentmanage.exception.DataNotFoundException;
import com.time.studentmanage.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.BadRequestException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
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
        // 학생 존재 여부 확인(학생 & 전화번호)
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
    public void checkIdDuplication(String userId) {
        // 아이디 검증
        boolean regexResult = userId.matches("^[a-z0-9]{6,20}$");

        if (!regexResult) {
            if (userId.length() < 6) {
                throw new IllegalArgumentException("아이디 길이가 6자 미만입니다.");
            }
            throw new IllegalArgumentException("아이디 형식을 확인해주세요.");
        }

        // 중복 검증
        // true (존재하는 경우)
        if (studentRepository.existsByUserId(userId)) {
            throw new IllegalArgumentException("이미 사용 중인 아이디입니다.");
        }
    }

    // 학생_정보_수정
    public void updateStudentInfo(Long id, StudentUpdateReqDto updateReqDto) {
        Optional<Student> studentOP = studentRepository.findById(id);

        if (!studentOP.isPresent()) {
            throw new DataNotFoundException("존재하지 않는 ID입니다.");
        }
        //더티체킹 발생
        studentOP.get().changeEntity(id, updateReqDto.toEntity());
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
    public List<StudentRespDto> getAllStudentsBySchoolName(String schoolName) {
        List<Student> studentList = studentRepository.findAllBySchoolNameOrderByGradeAsc(schoolName);

        List<StudentRespDto> resultDto = studentList.stream()
                .map(s -> createStudentRespDtoWithIdAndNameAndGrade(s.getId(), s.getName(), s.getGrade()))
                .collect(Collectors.toList());

        return resultDto;
    }

    private StudentRespDto createStudentRespDtoWithIdAndNameAndGrade(Long id, String name, Integer grade) {
        StudentRespDto studentRespDto = new StudentRespDto();
        studentRespDto.setId(id);
        studentRespDto.setName(name);
        studentRespDto.setGrade(grade);

        return studentRespDto;
    }

    @Transactional(readOnly = true)
    public List<StudentRespDto> getSearchedStudent(String content) {

        if (content.trim().equals("") || content == null) {
            throw new IllegalArgumentException("검색어가 입력되지 않았습니다.");
        }

        if (content.contains("+")) {
            String[] contentBits = content.split("\\+");

            String schoolName = contentBits[0].trim();
            String studentName = contentBits[1].trim();

            List<Student> searchedBySchoolNameAndStudentNameList = studentRepository.findAllBySearchEngine(schoolName,
                    studentName);

            List<StudentRespDto> respDtoList = createRespDtoList(searchedBySchoolNameAndStudentNameList);

            return respDtoList;
        }

        List<Student> searchedByStudentNameList = studentRepository.findAllBySearchEngine(null, content.trim());
        List<StudentRespDto> respDtoList = createRespDtoList(searchedByStudentNameList);

        return respDtoList;
    }

    private List<StudentRespDto> createRespDtoList(List<Student> targetList) {
        List<StudentRespDto> resultDto = targetList.stream()
                .map(s -> createStudentRespDtoWithSearchedResult(s.getId(), s.getName(), s.getGrade(),
                        s.getSchoolName()))
                .collect(Collectors.toList());

        return resultDto;
    }

    private StudentRespDto createStudentRespDtoWithSearchedResult(Long id, String name, Integer grade,
            String schoolName) {
        StudentRespDto studentRespDto = new StudentRespDto();
        studentRespDto.setId(id);
        studentRespDto.setName(name);
        studentRespDto.setGrade(grade);
        studentRespDto.setSchoolName(schoolName);

        return studentRespDto;
    }
    //학생 패스워드 변경
    public void updatePwd(Long studentId, String password) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(()-> new IllegalArgumentException("존재하지 않는 ID입니다."));
        //비밀번호 업데이트 (트랜잭션 종료 시 더티 체킹)
        student.changePassword(bCryptPasswordEncoder.encode(password));
    }
}
