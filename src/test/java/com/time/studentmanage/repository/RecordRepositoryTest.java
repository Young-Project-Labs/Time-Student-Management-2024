package com.time.studentmanage.repository;

import com.time.studentmanage.domain.Address;
import com.time.studentmanage.domain.Record;
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

import static com.time.studentmanage.TestUtil.*;
import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@DataJpaTest
@Transactional
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class RecordRepositoryTest {

    @Autowired
    RecordRepository recordRepository;

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
        Record record = createRecord(t, s);

        Record savedRecord = recordRepository.save(record);
        Record findRecord = recordRepository.findById(savedRecord.getId()).get();

        // then
        assertThat(findRecord.getContent()).isEqualTo(savedRecord.getContent());
    }

    @Test
    @Order(2)
    void 피드백_수정_테스트() {
        // given
        Student student = createStudent();
        Teacher teacher = createTeacher();

        Record record = createRecord(teacher, student);
        record.addStudent(student);
        record.addTeacher(teacher);

        // when
        Record savedRecord = recordRepository.save(record);
        log.info("savedRecord={}", savedRecord);
        Record findRecord = recordRepository.findById(savedRecord.getId()).get();
        log.info("findRecord={}", findRecord);

        findRecord.changeContent("철수가 문법 수준은 높으나 독해에 있어서 보충이 필요해 보입니다.");
        recordRepository.flush();

        //then
        Record updatedRecord = recordRepository.findById(findRecord.getId()).get();
        log.info("updatedRecord={}", updatedRecord);
        assertThat(updatedRecord.getContent()).isEqualTo(findRecord.getContent());
    }

    @Test
    @Order(3)
    void 피드백_삭제_테스트() {
        //given
        Student student = createStudent();
        Teacher teacher = createTeacher();
        Record record = createRecord(teacher, student);

        //when
        Record savedRecord = recordRepository.save(record);
        Record findRecord = recordRepository.findById(savedRecord.getId()).get();
        findRecord.changeRecordStatus(RecordStatus.DELETED);

        recordRepository.flush();

        //then
        Record updatedRecord = recordRepository.findById(findRecord.getId()).get();
        assertThat(updatedRecord.getStatus()).isEqualTo(RecordStatus.DELETED);
        assertThat(recordRepository.findAllByStatusAndStudent(RecordStatus.PUBLISHED, student).size()).isEqualTo(0);
    }
}