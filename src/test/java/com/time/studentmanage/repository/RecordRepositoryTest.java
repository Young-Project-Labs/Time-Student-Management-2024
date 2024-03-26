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
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static com.time.studentmanage.TestUtil.createRecord;
import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@DataJpaTest
@Transactional
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class RecordRepositoryTest {

    @Autowired
    RecordRepository recordRepository;
    @Autowired
    StudentRepository studentRepository;
    @Autowired
    TeacherRepository teacherRepository;


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
    void 특정_내용을_포함하는_레코드_검색_테스트() {
        //given
        Student s = Student.builder()
                .name("철수")
                .build();

        Teacher t = Teacher.builder()
                .name("줄리아")
                .build();

        studentRepository.save(s);
        teacherRepository.save(t);

        Record record1 = Record.builder()
                .teacher(t)
                .student(s)
                .status(RecordStatus.PUBLISHED)
                .content("가나다라")
                .build();
        recordRepository.save(record1);

        Record record2 = Record.builder()
                .teacher(t)
                .student(s)
                .status(RecordStatus.PUBLISHED)
                .content("마바사")
                .build();
        recordRepository.save(record2);

        Record record3 = Record.builder()
                .teacher(t)
                .student(s)
                .status(RecordStatus.PUBLISHED)
                .content("아자차")
                .build();
        recordRepository.save(record3);

        // when
        List<Record> result = recordRepository.findAllByStatusAndStudent(RecordStatus.PUBLISHED, s);
        List<Record> filteredResult1 = recordRepository.findAllByContent(RecordStatus.PUBLISHED, s, "%나다%");
        List<Record> filteredResult2 = recordRepository.findAllByContent(RecordStatus.PUBLISHED, s, "%차%");

        // then
        assertThat(result.size()).isEqualTo(3);

        assertThat(filteredResult1.size()).isEqualTo(1);
        assertThat(filteredResult1.get(0).getContent()).isEqualTo(record1.getContent());

        assertThat(filteredResult2.size()).isEqualTo(1);
        assertThat(filteredResult2.get(0).getContent()).isEqualTo(record3.getContent());

    }

    @Test
    void 특정_선생님이_작성한_레코드들을_필터링_테스트() {
        //given
        Student s = Student.builder()
                .name("철수")
                .build();

        Teacher t1 = Teacher.builder()
                .name("줄리아")
                .build();
        Teacher t2 = Teacher.builder()
                .name("안샘")
                .build();
        Teacher t3 = Teacher.builder()
                .name("대박샘")
                .build();

        studentRepository.save(s);
        teacherRepository.save(t1);
        teacherRepository.save(t2);
        teacherRepository.save(t3);

        Record record1 = Record.builder()
                .teacher(t1)
                .student(s)
                .status(RecordStatus.PUBLISHED)
                .content("가나다라")
                .build();
        recordRepository.save(record1);

        Record record2 = Record.builder()
                .teacher(t2)
                .student(s)
                .status(RecordStatus.PUBLISHED)
                .content("마바사")
                .build();
        recordRepository.save(record2);

        Record record3 = Record.builder()
                .teacher(t3)
                .student(s)
                .status(RecordStatus.PUBLISHED)
                .content("아자차")
                .build();
        recordRepository.save(record3);

        Record record4 = Record.builder()
                .teacher(t1)
                .student(s)
                .status(RecordStatus.PUBLISHED)
                .content("카타파하")
                .build();
        recordRepository.save(record4);

        Record record5 = Record.builder()
                .teacher(t3)
                .student(s)
                .status(RecordStatus.DELETED)
                .content("ABCDEEEE")
                .build();
        recordRepository.save(record5);

        //when
        List<Record> result1 = recordRepository.findAllByTeacherName(RecordStatus.PUBLISHED, s, t1.getName());
        List<Record> result2 = recordRepository.findAllByTeacherName(RecordStatus.PUBLISHED, s, t3.getName());

        //then
        assertThat(result1.size()).isEqualTo(2);
        assertThat(result1.get(0).getTeacher().getName()).isEqualTo(t1.getName());
        assertThat(result2.size()).isEqualTo(1);
        assertThat(result2.get(0).getContent()).isEqualTo(record3.getContent());

    }

    @Test
    void 특정_기간에_작성한_모든_피드백_조회_테스트() {
        //given
        Student s = Student.builder()
                .name("철수")
                .build();

        Teacher t1 = Teacher.builder()
                .name("줄리아")
                .build();
        Teacher t2 = Teacher.builder()
                .name("안샘")
                .build();
        Teacher t3 = Teacher.builder()
                .name("대박샘")
                .build();

        studentRepository.save(s);
        teacherRepository.save(t1);
        teacherRepository.save(t2);
        teacherRepository.save(t3);

        Record record1 = Record.builder()
                .teacher(t1)
                .student(s)
                .status(RecordStatus.PUBLISHED)
                .content("가나다라")
                .build();
        recordRepository.save(record1);
        ReflectionTestUtils.setField(record1, "createDate", LocalDateTime.of(2024, 1, 1, 0, 0));

        Record record2 = Record.builder()
                .teacher(t2)
                .student(s)
                .status(RecordStatus.PUBLISHED)
                .content("마바사")
                .build();
        recordRepository.save(record2);
        ReflectionTestUtils.setField(record2, "createDate", LocalDateTime.of(2024, 2, 1, 0, 0));


        Record record3 = Record.builder()
                .teacher(t3)
                .student(s)
                .status(RecordStatus.PUBLISHED)
                .content("아자차")
                .build();
        recordRepository.save(record3);
        ReflectionTestUtils.setField(record3, "createDate", LocalDateTime.of(2024, 3, 1, 0, 0));

        Record record4 = Record.builder()
                .teacher(t1)
                .student(s)
                .status(RecordStatus.PUBLISHED)
                .content("카타파하")
                .build();
        recordRepository.save(record4);
        ReflectionTestUtils.setField(record4, "createDate", LocalDateTime.of(2024, 3, 15, 0, 0));


        Record record5 = Record.builder()
                .teacher(t3)
                .student(s)
                .status(RecordStatus.DELETED)
                .content("ABCDEEEE")
                .build();
        recordRepository.save(record5);
        ReflectionTestUtils.setField(record5, "createDate", LocalDateTime.of(2024, 3, 25, 0, 0));


        //when
        List<Record> filteredResult = recordRepository.findAllBySelectedDateRange(RecordStatus.PUBLISHED, s,
                LocalDateTime.of(2024, 3, 1, 0, 0, 0));

        for (Record record : filteredResult) {
            log.info("getContent={}", record.getContent());
            log.info("getContent={}", record.getCreateDate());
        }

        //then
        assertThat(filteredResult.size()).isEqualTo(3);
        assertThat(filteredResult.get(0).getContent()).isEqualTo(record5.getContent());
    }
}