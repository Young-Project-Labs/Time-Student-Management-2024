package com.time.studentmanage.repository;

import com.time.studentmanage.domain.Address;
import com.time.studentmanage.domain.Records;
import com.time.studentmanage.domain.enums.*;
import com.time.studentmanage.domain.member.Student;
import com.time.studentmanage.domain.member.Teacher;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@SpringBootTest
@Transactional
class RecordsRepositoryTest {

    @Autowired
    TeacherRepository teacherRepository;

    @Autowired
    StudentRepository studentRepository;

    @Autowired
    RecordsRepository recordsRepository;

    @Test
    @Transactional(readOnly = true)
    void 피드백_생성_조회_테스트() {
        Student s = new Student("철수", "cs@time.com", "1234", "010-1111-2222", "용호초등학교", ClassType.ELEMENTARY, 1, MemberType.STUDENT, GenderType.MALE, new Address("반림동", "현대 아파트", "102-1201"), AttendanceStatus.Y);
        Teacher t = new Teacher("줄리아", "julia@time.com", "1234", "010-1212-3456", MemberType.TEACHER, Position.TEACHER, "julia@time.com", GenderType.FEMALE);
        t.addStudent(s);
        studentRepository.save(s);
        teacherRepository.save(t);

        // given
        Teacher teacher = teacherRepository.findById(s.getId()).get();
        Student student = studentRepository.findById(t.getId()).get();

        // when
        Records records = new Records(teacher, student, "철수의 문법 수준이 높습니다. 테스트 후 초등 고학년 문법반으로 올려도 될 것 같습니다.");
        recordsRepository.save(records);

        Records findRecord = recordsRepository.findById(records.getId()).get();
        List<Records> recordsList = recordsRepository.findAll();

        // then
        assertThat(findRecord.getContent()).isEqualTo(records.getContent());
        assertThat(recordsList.size()).isEqualTo(1);
    }

    @Test
    void 피드백_수정_테스트() {
        Student s = createStudent();
        Teacher t = createTeacher();
        t.addStudent(s);
//        studentRepository.save(s);
//        teacherRepository.save(t);

        // given
        Student findStudent = studentRepository.findById(s.getId()).get(); // id를 부여할 때에 db에 갱신되는 값이 들어가기 때문에 테스트 시 저장 후 가지고 있는 객체의 id 값을 가져와서 테스트 해야한다.
        Teacher findTeacher = teacherRepository.findById(t.getId()).get();


        Records records = new Records(findTeacher, findStudent, "철수의 문법 수준이 높습니다. 테스트 후 초등 고학년 문법반으로 올려도 될 것 같습니다.");
        records.addStudent(findStudent);
        records.addTeacher(findTeacher);
        recordsRepository.save(records);
        Records findRecord = recordsRepository.findById(records.getId()).get();


        //then
        findRecord.setContent("철수가 문법 수준은 높으나 독해에 있어서 보충이 필요해 보입니다.");
        recordsRepository.save(findRecord);

        Records updatedRecord = recordsRepository.findById(findRecord.getId()).get();
        assertThat(updatedRecord.getContent()).isEqualTo(findRecord.getContent());
    }

    private Student createStudent() {
        Student s = new Student("철수", "cs@time.com", "1234", "010-1111-2222", "용호초등학교", ClassType.ELEMENTARY, 1, MemberType.STUDENT, GenderType.MALE, new Address("반림동", "현대 아파트", "102-1201"), AttendanceStatus.Y);
        Student student = studentRepository.save(s);
        return student;
    }

    private Teacher createTeacher() {
        Teacher t = new Teacher("줄리아", "julia@time.com", "1234", "010-1212-3456", MemberType.TEACHER, Position.TEACHER, "julia@time.com", GenderType.FEMALE);
        Teacher teacher = teacherRepository.save(t);
        return teacher;
    }
}