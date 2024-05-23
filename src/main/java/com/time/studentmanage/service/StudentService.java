package com.time.studentmanage.service;

import com.time.studentmanage.domain.classroom.ClassRoom;
import com.time.studentmanage.domain.dto.classroom.ClassStudentRespDto;
import com.time.studentmanage.domain.dto.student.*;
import com.time.studentmanage.domain.enums.AttendanceStatus;
import com.time.studentmanage.domain.enums.ProviderType;
import com.time.studentmanage.domain.member.Student;
import com.time.studentmanage.exception.DataNotFoundException;
import com.time.studentmanage.repository.student.StudentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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

    //학생_재원여부_변경
    public void editAttendanceStatus(Long id) {
        Optional<Student> studentOP = studentRepository.findById(id);
        if (!studentOP.isPresent()) {
            throw new DataNotFoundException("존재하지 않는 학생입니다.");
        }

        Student student = studentOP.get();
        if (student.getAttendanceStatus().equals(AttendanceStatus.Y)) {
            //상태 변경
            student.changeAttendanceStatus(AttendanceStatus.N);
        } else {
            //상태 변경
            student.changeAttendanceStatus(AttendanceStatus.Y);
        }

    }


    // 학생_삭제
    public void deleteStudent(Long id) {
        Optional<Student> studentOP = studentRepository.findById(id);
        if (!studentOP.isPresent()) {
            throw new DataNotFoundException("존재하지 않는 학생입니다.");
        }

        studentRepository.delete(studentOP.get());
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
    public Page<SelectedSchoolRespDto> getHomePageSearchResult(String schoolName, String studentName, int page) {
        Pageable pageable = PageRequest.of(page, 10);
        Page<SelectedSchoolRespDto> pagingResult = studentRepository.findAllBySelectedSchoolName(schoolName, studentName, pageable);

        if (pagingResult == null || pagingResult.isEmpty()) {
            throw new DataNotFoundException("검색 결과가 존재하지 않습니다.");
        }

        return pagingResult;
    }

    @Transactional(readOnly = true)
    public Page<StudentSearchRespDto> getSearchedResult(StudentSearchReqDto studentSearchReqDto) {
        if (studentSearchReqDto.getPage() < 0) {
            throw new IllegalArgumentException("잘못된 페이지 요청 입니다.");
        }

        Pageable pageable = PageRequest.of(studentSearchReqDto.getPage(), 10);

        Page<StudentSearchRespDto> pagingResult = studentRepository.findAllBySearchEngine(studentSearchReqDto, pageable);

        return pagingResult;
    }

    //학생 패스워드 변경
    public void updatePwd(String userId, String password) {
        Student student = studentRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 ID입니다."));
        //비밀번호 업데이트 (트랜잭션 종료 시 더티 체킹)
        student.changePassword(bCryptPasswordEncoder.encode(password));
    }

    public List<ClassStudentRespDto> getSearchedStudentNotIncludeClassRoom(String content) {
        List<Student> studentList = studentRepository.findAllBySearchEngineWithNameNotIncludeClass(content);

        List<ClassStudentRespDto> resultDtoList = studentList.stream()
                .map(s -> new ClassStudentRespDto(s.getId(), s.getSchoolName(), s.getGrade(), s.getName(), s.getPhoneNumber()))
                .collect(Collectors.toList());

        if (resultDtoList == null || resultDtoList.size() == 0) {
            throw new DataNotFoundException("학생 정보가 없거나 이미 추가된 학생 입니다.");
        }
        return resultDtoList;
    }

    public void connectClassRoom(Long id, ClassRoom classRoom) {
        Optional<Student> studentOp = studentRepository.findById(id);
        if (!studentOp.isPresent()) {
            throw new DataNotFoundException("해당하는 학생이 없습니다.");
        }
        Student studentPs = studentOp.get();
        studentPs.addClassRoom(classRoom);
    }

    public void disconnectClassRoom(Long studentId) {
        Optional<Student> studentOp = studentRepository.findById(studentId);
        if (!studentOp.isPresent()) {
            throw new DataNotFoundException("해당하는 학생이 없습니다.");
        }
        Student studentPs = studentOp.get();
        studentPs.removeClassRoom();
    }

    //카카오 회원가입 여부 체크
    public Optional<Student> checkJoinKakaoStudent(String userId) {
        Optional<Student> studentOP = studentRepository.findByUserIdAndProvider(userId, ProviderType.KAKAO);
        return studentOP;
    }

    //카카오 회원가입
    public Long saveStudentKaKao(KakaoSaveReqDto kakaoSaveReqDto) {
        Student saveStudent = studentRepository.save(kakaoSaveReqDto.toEntity());
        return saveStudent.getId();
    }

    public Optional<Student> findId(FindIdDto findIdDto) {
        Optional<Student> studentOP = studentRepository.findByNameAndEmail(findIdDto.getName(), findIdDto.getEmail());
        return studentOP;
    }

}