package com.time.studentmanage.repository;

import com.time.studentmanage.domain.dto.student.SelectedSchoolRespDto;
import com.time.studentmanage.domain.dto.student.StudentSearchReqDto;
import com.time.studentmanage.domain.dto.student.StudentSearchRespDto;
import com.time.studentmanage.domain.enums.AttendanceStatus;
import com.time.studentmanage.domain.enums.ClassType;
import com.time.studentmanage.domain.enums.SearchType;
import com.time.studentmanage.domain.member.Address;
import com.time.studentmanage.domain.member.Student;
import com.time.studentmanage.domain.member.Teacher;
import com.time.studentmanage.domain.record.Record;
import com.time.studentmanage.repository.student.StudentRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.time.studentmanage.TestUtil.*;
import static org.assertj.core.api.Assertions.assertThat;

@Transactional
@DataJpaTest
@Slf4j
@TestMethodOrder(MethodOrderer.MethodName.class)
class StudentRepositoryTest {

    @Autowired
    StudentRepository studentRepository;

    @Test
    @Order(1)
    void 학생_등록_테스트() {
        //given
        Student student = createStudent();

        //when
        Student saveStudent = studentRepository.save(student);

        //then
        Optional<Student> findStudent = studentRepository.findById(saveStudent.getId());
        if (findStudent.isPresent()) {
            log.info("findStudent={}", findStudent.get().getId());
            assertThat(saveStudent.getId()).isEqualTo(findStudent.get().getId());
        }

    }

    @Test
    @Order(2)
    void 학생_전체_조회_테스트() {
        //given
        Student student1 = createStudent();
        Student student2 = createStudent();
        Student student3 = createStudent();
        studentRepository.save(student1);
        studentRepository.save(student2);
        studentRepository.save(student3);

        //when
        List<Student> studentList = studentRepository.findAll();

        //then
        for (Student student : studentList) {
            log.info("student={}", student.getId());
        }
        assertThat(studentList.size()).isEqualTo(3);

    }

    @Test
    @Order(3)
    void 학교별_학생_조회_테스트() {
        //given
        List<Student> studentList = createManyStudent();
        List<Student> saveStudentList = studentRepository.saveAll(studentList);
        String schoolName = "용호초등학교";
        //when
        List<Student> findStudentList = studentRepository.findBySchoolNameOrderByGrade(schoolName);
        for (Student student : findStudentList) {
            log.info("student={}", student);
        }
        //then
        assertThat(findStudentList.size()).isEqualTo(2);
    }

    @Test
    @Order(4)
    void 학년별_학생_조회_테스트() {
        //given
        List<Student> studentList = createManyStudent();
        List<Student> saveStudentList = studentRepository.saveAll(studentList);
        String schoolName = "용호초등학교";
        int grade = 1;
        //when
        List<Student> findStudentList = studentRepository.findBySchoolNameAndGrade(schoolName, grade);

        //then
        assertThat(findStudentList.size()).isEqualTo(1);
    }

    @Test
    @Order(5)
    void 학생_상세_정보_조회_테스트() {
        //given
        Student student = createStudent();

        Teacher teacher = createTeacher();

        Record record = createRecord(teacher, student);
        record.addStudent(student);

        Record record2 = createRecord(teacher, student);
        record2.addStudent(student);

        Student savedStudent = studentRepository.save(student);

        //when
        Optional<Student> findStudent = studentRepository.findById(savedStudent.getId());
        //then
        assertThat(findStudent.get().getName()).isEqualTo("철수");
    }

    @Test
    @Order(6)
    void 학생_정보_수정_테스트() {
        //given
        Student student = createStudent();
        Student saveStudent = studentRepository.save(student);
        log.info("수정 전 이름={}", saveStudent.getName());
        Student findStudent = studentRepository.findById(saveStudent.getId()).get();
        log.info("수정 전 엔티티 확인={}", findStudent.toString());
        //when
        findStudent.changeName("영진");
        Address changeAd = new Address("창원시", "의창구 동정동 409", "1층");
        findStudent.changeAddress(changeAd);
        findStudent.changeClassType(ClassType.MIDDLE);
        studentRepository.flush();

        //then
        Student validationStudent = studentRepository.findById(findStudent.getId()).get();
        log.info("수정 후 이름={}", validationStudent.getName());
        log.info("수정 후 엔티티 확인={}", validationStudent.toString());
        assertThat(validationStudent.getName()).isEqualTo(findStudent.getName());
        assertThat(validationStudent.getAddress()).isEqualTo(changeAd);
        assertThat(validationStudent.getClassType()).isEqualTo(findStudent.getClassType());

    }

    @Test
    @Order(7)
    void 학생_재원_여부_변경_테스트() {
        //given
        List<Student> studentList = createManyStudent();
        List<Student> test = studentRepository.saveAll(studentList);

        List<Student> findStudentList = studentRepository.findAll();
        Student student1 = findStudentList.get(0);
        log.info("수정 전 getAttendanceStatus={}", student1.getAttendanceStatus());
        //when
        student1.changeAttendanceStatus(AttendanceStatus.N);
        studentRepository.flush();
        log.info("수정 후 getAttendanceStatus={}", student1.getAttendanceStatus());

        //then
        Student validationStudent = studentRepository.findById(findStudentList.get(0).getId()).get();

        assertThat(validationStudent.getAttendanceStatus()).isEqualTo(AttendanceStatus.N);
    }

    @Test
    @Order(8)
    void 학생_삭제_테스트() {
        //given
        List<Student> studentList = createManyStudent();
        List<Student> saveStudentList = studentRepository.saveAll(studentList);
        //when
        studentRepository.deleteById(studentList.get(0).getId());
        //then
        List<Student> deleteAfterStudentList = studentRepository.findAll();
        for (Student student : deleteAfterStudentList) {
            log.info("삭제 후 학생 확인 : " + student.getName());
        }
        assertThat(studentRepository.findAll().size()).isEqualTo(4);
    }

    @Test
    void 학생_ID_조회_테스트() {
        //given
        Student student = createStudent();
        String newId = "cs@time.com";
        studentRepository.save(student);
        //when
        Boolean exists = studentRepository.existsByUserId(newId);
        log.info("exists={}", exists);
        //then
        assertThat(exists).isTrue();

    }

    @Test
    void 학교별_학년_오름차순으로_학생_조회_JPA메서드_테스트() {
        //given
        for (int i = 1; i <= 6; i++) {
            Student student = Student.builder()
                    .name("용호초 학생" + i)
                    .grade(i)
                    .schoolName("용호초등학교")
                    .build();

            studentRepository.save(student);
        }

        for (int i = 1; i <= 6; i++) {
            Student student = Student.builder()
                    .name("반송초 학생" + i)
                    .grade(i)
                    .schoolName("반송초등학교")
                    .build();

            studentRepository.save(student);
        }

        //when
        List<Student> result = studentRepository.findAllBySchoolNameOrderByGradeAsc("용호초등학교");

        for (int i = 0; i < result.size(); i++) {
            log.info("student={}", result.get(i).getName());
        }

        //then
        assertThat(result.size()).isEqualTo(6);
        assertThat(result.get(0).getGrade()).isEqualTo(1);
        assertThat(result.get(2).getGrade()).isEqualTo(3);
    }

    @Test
    void 저장된_모든_학교_이름을_리스트로_조회_테스트() {
        //given
        for (int i = 5; i >= 0; i--) {
            char schoolNameIdx = 'A';
            Student student = Student.builder()
                    .name("학생" + i)
                    .grade(i)
                    .schoolName(String.format("학교" + (char) (schoolNameIdx + i)))
                    .build();

            studentRepository.save(student);
        }

        Student studentZ = Student.builder()
                .name("학생Z")
                .grade(6)
                .schoolName("학교Z")
                .build();

        Student studentZZ = Student.builder()
                .name("학생ZZ")
                .grade(5)
                .schoolName("학교Z")
                .build();

        studentRepository.save(studentZ);
        studentRepository.save(studentZZ);

        //when
        List<String> result = studentRepository.findAllSchoolName();

        for (int i = 0; i < result.size(); i++) {
            log.info("result={}", result.get(i));
        }

        //then
        assertThat(result.size()).isEqualTo(7);
        assertThat(result.get(result.size() - 1)).isEqualTo(studentZ.getSchoolName());
    }

    @Test
    void 검색엔진_조회_테스트_학생이름() {
        //given
        makeDummyData();

        //when
        Pageable pageable = PageRequest.of(0, 8);
        StudentSearchReqDto studentSearchReqDto = new StudentSearchReqDto();
        studentSearchReqDto.setSearchType(SearchType.STUDENT_NAME);
        studentSearchReqDto.setContent("학생3");

        Page<StudentSearchRespDto> result = studentRepository.findAllBySearchEngine(studentSearchReqDto, pageable);

        //then
        List<StudentSearchRespDto> resultList = result.get().collect(Collectors.toList());
        assertThat(resultList.size()).isEqualTo(3);
        assertThat(resultList.get(0).getName()).contains("학생3");
    }

    @Test
    void 검색엔진_조회_테스트_부모이름() {
        //given
        makeDummyData();

        //when
        Pageable pageable = PageRequest.of(0, 8);
        StudentSearchReqDto studentSearchReqDto = new StudentSearchReqDto();
        studentSearchReqDto.setSearchType(SearchType.PARENT_NAME);
        studentSearchReqDto.setContent("학생3부모");

        Page<StudentSearchRespDto> result = studentRepository.findAllBySearchEngine(studentSearchReqDto, pageable);

        //then
        List<StudentSearchRespDto> resultList = result.get().collect(Collectors.toList());
        assertThat(resultList.size()).isEqualTo(3);
        assertThat(resultList.get(0).getParentName()).isEqualTo("C고등학교 학생3부모님");
    }

    @Test
    void 검색엔진_조회_테스트_학교이름() {
        //given
        makeDummyData();

        //when
        Pageable pageable = PageRequest.of(0, 8);
        StudentSearchReqDto studentSearchReqDto = new StudentSearchReqDto();
        studentSearchReqDto.setSearchType(SearchType.SCHOOL_NAME);
        studentSearchReqDto.setContent("A초등학교");

        Page<StudentSearchRespDto> result = studentRepository.findAllBySearchEngine(studentSearchReqDto, pageable);

        //then
        List<StudentSearchRespDto> resultList = result.get().collect(Collectors.toList());
        assertThat(resultList.size()).isEqualTo(8);
    }

    private void makeDummyData() {
        for (int i = 0; i < 10; i++) {
            Student studentA = Student.builder()
                    .name("A초등학교 학생" + (i + 1))
                    .attendanceStatus(AttendanceStatus.Y)
                    .schoolName("A초등학교")
                    .parentName("A초등학교 학생" + (i + 1) + "부모님")
                    .build();
            Student studentB = Student.builder()
                    .name("B중학교 학생" + (i + 1))
                    .attendanceStatus(AttendanceStatus.Y)
                    .schoolName("B중학교")
                    .parentName("B중학교 학생" + (i + 1) + "부모님")
                    .build();
            Student studentC = Student.builder()
                    .name("C고등학교 학생" + (i + 1))
                    .attendanceStatus(AttendanceStatus.Y)
                    .schoolName("C고등학교")
                    .parentName("C고등학교 학생" + (i + 1) + "부모님")
                    .build();
            studentRepository.save(studentA);
            studentRepository.save(studentB);
            studentRepository.save(studentC);
        }
    }

    @Test
    void 선택한_학교에_해당하는_학생_전체_조회_테스트() {
        //given
        for (int i = 0; i < 30; i++) {
            Student student = Student.builder()
                    .name("학생" + (i + 1))
                    .attendanceStatus(AttendanceStatus.Y)
                    .schoolName("테스트 학교")
                    .grade((int) (Math.random() * 6) + 1)
                    .build();
            studentRepository.save(student);
        }
        //when

        Pageable pageable = PageRequest.of(1, 8);
        Page<SelectedSchoolRespDto> result = studentRepository.findAllBySelectedSchoolName("테스트 학교", null, pageable);

        log.info("result={}", result);

        //then
        assertThat(result.get().collect(Collectors.toList()).size()).isEqualTo(pageable.getPageSize());
        assertThat(result.get().collect(Collectors.toList()).get(0).getGrade()).isLessThanOrEqualTo(result.get().collect(Collectors.toList()).get(7).getGrade());
    }
}