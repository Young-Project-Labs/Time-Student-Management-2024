package com.time.studentmanage.repository;

import com.time.studentmanage.TestUtil;
import com.time.studentmanage.domain.Address;
import com.time.studentmanage.domain.Records;
import com.time.studentmanage.domain.enums.*;
import com.time.studentmanage.domain.member.Student;
import com.time.studentmanage.domain.member.Teacher;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static com.time.studentmanage.TestUtil.*;
import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@DataJpaTest
@Transactional
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class RecordsRepositoryTest {

    @Autowired
    RecordsRepository recordsRepository;

    @Test
    @Transactional(readOnly = true)
    @Order(1)
    void 피드백_생성_조회_테스트() {

        // given
        Student s = Student.builder()
                .name("철수")
                .userId("cs@time.com").password("1234")
                .phoneNumber("010-1111-2222").schoolName("용호초등학교")
                .classType(ClassType.ELEMENTARY).grade(1)
                .memberType(MemberType.STUDENT).gender(GenderType.MALE)
                .address(new Address("반림동", "현대 아파트", "102-1201"))
                .attendanceStatus(AttendanceStatus.Y)
                .build();
        Teacher t = new Teacher("줄리아", "julia@time.com", "1234", "010-1212-3456", MemberType.TEACHER, Position.TEACHER, "julia@time.com", GenderType.FEMALE);

        // when
        Records record = createRecord(t, s);

        Records savedRecord = recordsRepository.save(record);
        Records findRecord = recordsRepository.findById(savedRecord.getId()).get();

        // then
        assertThat(findRecord.getContent()).isEqualTo(savedRecord.getContent());
    }

    @Test
    @Order(2)
    void 피드백_수정_테스트() {
        // given
        Student student = createStudent();
        Teacher teacher = createTeacher();

        Records records = createRecord(teacher, student);
        records.addStudent(student);
        records.addTeacher(teacher);

        // when
        Records savedRecord = recordsRepository.save(records);
        log.info("savedRecord={}", savedRecord);
        Records findRecord = recordsRepository.findById(savedRecord.getId()).get();
        log.info("findRecord={}", findRecord);

        findRecord.changeContent("철수가 문법 수준은 높으나 독해에 있어서 보충이 필요해 보입니다.");
        recordsRepository.flush();

        //then
        Records updatedRecord = recordsRepository.findById(findRecord.getId()).get();
        log.info("updatedRecord={}", updatedRecord);
        assertThat(updatedRecord.getContent()).isEqualTo(findRecord.getContent());
    }

    @Test
    @Order(3)
    void 피드백_삭제_테스트() {
        //given
        Student student = createStudent();
        Teacher teacher = createTeacher();
        Records record = createRecord(teacher, student);

        //when
        Records savedRecord = recordsRepository.save(record);
        recordsRepository.delete(savedRecord);

        //then
        assertThat(recordsRepository.findById(savedRecord.getId())).isEqualTo(Optional.empty());
        assertThat(recordsRepository.findAll().size()).isEqualTo(0);
    }


    @Test
    @Order(4)
    void 특정_학생_이름과_관련된_모든_피드백_조회() {
        //given
        Student student = createStudent();
        Teacher teacher = createTeacher();
        Student student2 = Student.builder()
                .name("노진구")
                .userId("njk@time.com").password("1234")
                .phoneNumber("010-4444-5555").schoolName("용호중학교")
                .classType(ClassType.MIDDLE).grade(3)
                .memberType(MemberType.STUDENT).gender(GenderType.MALE)
                .address(new Address("반림동", "현대 아파트", "102-342"))
                .attendanceStatus(AttendanceStatus.Y)
                .build();

        for (int i = 0; i < 5; i++) {
            Records record = Records.builder()
                    .teacher(teacher).student(student).content("철수 피드백" + i)
                    .build();

            Records record2 = Records.builder()
                    .teacher(teacher).student(student2).content("노진구 피드백" + i)
                    .build();

            record.addStudent(student);
            record.addTeacher(teacher);
            record2.addStudent(student2);
            record2.addTeacher(teacher);

            recordsRepository.save(record);
            recordsRepository.save(record2);
        }

    }


    private Student createStudent() {
        Student student = Student.builder()
                .name("철수")
                .userId("cs@time.com").password("1234")
                .phoneNumber("010-1111-2222").schoolName("용호초등학교")
                .classType(ClassType.ELEMENTARY).grade(1)
                .memberType(MemberType.STUDENT).gender(GenderType.MALE)
                .address(new Address("반림동", "현대 아파트", "102-1201"))
                .attendanceStatus(AttendanceStatus.Y)
                .build();
        return student;
    }

    private Teacher createTeacher() {
        Teacher teacher = new Teacher("줄리아", "julia@time.com", "1234", "010-1212-3456", MemberType.TEACHER, Position.TEACHER, "julia@time.com", GenderType.FEMALE);
        return teacher;
    }
}