package com.time.studentmanage.repository;

import com.time.studentmanage.TestUtil;
import com.time.studentmanage.domain.Address;
import com.time.studentmanage.domain.Records;
import com.time.studentmanage.domain.enums.*;
import com.time.studentmanage.domain.member.Parent;
import com.time.studentmanage.domain.member.Student;
import com.time.studentmanage.domain.member.Teacher;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.support.JstlUtils;

import javax.swing.text.Style;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.time.studentmanage.TestUtil.*;
import static com.time.studentmanage.TestUtil.createRecord;
import static com.time.studentmanage.TestUtil.createTeacher;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
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
            log.info("findStudent={}",findStudent.get().getId());
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
        for (Student student: studentList) {
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
        assertThat(findStudentList.size()).isEqualTo(3);
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

        Records record = createRecord(teacher, student);
        record.addStudent(student);

        Records record2 = createRecord(teacher, student);
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
        log.info("수정 후 이름={}",validationStudent.getName());
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
        log.info("수정 전 getAttendanceStatus={}",student1.getAttendanceStatus());
        //when
        student1.changeAttendanceStatus(AttendanceStatus.N);
        studentRepository.flush();
        log.info("수정 후 getAttendanceStatus={}",student1.getAttendanceStatus());

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





}