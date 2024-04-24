package com.time.studentmanage.repository.record;

import com.time.studentmanage.domain.dto.record.RecordRespDto;
import com.time.studentmanage.domain.enums.*;
import com.time.studentmanage.domain.member.Address;
import com.time.studentmanage.domain.member.Student;
import com.time.studentmanage.domain.member.Teacher;
import com.time.studentmanage.domain.record.Record;
import com.time.studentmanage.repository.student.StudentRepository;
import com.time.studentmanage.repository.teacher.TeacherRepository;
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
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

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
        Teacher t = new Teacher("줄리아", "1234", "010-1212-3456", MemberType.TEACHER, Position.TEACHER, "julia@time.com", GenderType.FEMALE);

        // when
        Record record = createRecord(t, s);

        Record savedRecord = recordRepository.save(record);
        Record findRecord = recordRepository.findById(savedRecord.getId()).get();

        // then
        assertThat(findRecord.getContent()).isEqualTo(savedRecord.getContent());
    }

    public void createDummyRecordData(Student student, Teacher teacher) {
        for (int i = 0; i < 10; i++) {
            Record record = Record.builder()
                    .student(student)
                    .teacher(teacher)
                    .content((i + 1) + "번 테스트 피드백 입니다.")
                    .status(RecordStatus.PUBLISHED)
                    .build();
            record.addStudent(student);
            record.addTeacher(teacher);

            recordRepository.save(record);
        }
    }

    @Test
    void 페이징_처리_피드백_조회_테스트() {
        //given
        Student s = Student.builder()
                .name("철수")
                .userId("cs@time.com").password("1234")
                .phoneNumber("010-1111-2222").schoolName("용호초등학교")
                .classType(ClassType.ELEMENTARY).grade(1)
                .memberType(MemberType.STUDENT).gender(GenderType.MALE)
                .address(new Address("반림동", "현대 아파트", "102-1201"))
                .attendanceStatus(AttendanceStatus.Y)
                .build();
        Teacher t = new Teacher("줄리아", "1234", "010-1212-3456", MemberType.TEACHER, Position.TEACHER, "julia@time.com", GenderType.FEMALE);

        Student student = studentRepository.save(s);
        Teacher teacher = teacherRepository.save(t);

        createDummyRecordData(student, teacher);

        //when
        Pageable pageable = PageRequest.of(1, 8);
        Page<RecordRespDto> result = recordRepository.findAllPaging(student, pageable);

        //then
        log.info("result={}", result);
        assertThat(result.get().collect(Collectors.toList()).size()).isEqualTo(2);

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
        Pageable pageable = PageRequest.of(0, 8);
        List<Record> result = recordRepository.findAllByStatusAndStudent(RecordStatus.PUBLISHED, s);
        Page<RecordRespDto> filteredResult1 = recordRepository.findAllByContentSearch(s, "나다", null, null, pageable);
        Page<RecordRespDto> filteredResult2 = recordRepository.findAllByContentSearch(s, "차", null, null, pageable);

        // then
        assertThat(result.size()).isEqualTo(3);
        assertThat(filteredResult1.get().collect(Collectors.toList()).size()).isEqualTo(1);
        assertThat(filteredResult1.get().collect(Collectors.toList()).get(0).getContent()).isEqualTo(record1.getContent());

        assertThat(filteredResult2.get().collect(Collectors.toList()).size()).isEqualTo(1);
        assertThat(filteredResult2.get().collect(Collectors.toList()).get(0).getContent()).isEqualTo(record3.getContent());
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
        Pageable pageable = PageRequest.of(0, 8);
        Page<RecordRespDto> result1 = recordRepository.findAllByTeacherNameSearch(s, t1.getName(), null, null, pageable);
        Page<RecordRespDto> result2 = recordRepository.findAllByTeacherNameSearch(s, t3.getName(), null, null, pageable);

        //then
        assertThat(result1.get().collect(Collectors.toList()).size()).isEqualTo(2);
        assertThat(result1.get().collect(Collectors.toList()).get(0).getTeacherName()).isEqualTo(t1.getName());
        assertThat(result2.get().collect(Collectors.toList()).size()).isEqualTo(1);
        assertThat(result2.get().collect(Collectors.toList()).get(0).getContent()).isEqualTo(record3.getContent());
    }

    @Test
    void 특정_검색어를_포함하지_않고_특정_날짜만_포함하는_피드백_조회_테스트() {
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
        ReflectionTestUtils.setField(record4, "createDate", LocalDateTime.of(2024, 3, 15, 23, 15));

        Record record5 = Record.builder()
                .teacher(t3)
                .student(s)
                .status(RecordStatus.DELETED)
                .content("ABCDEEEE")
                .build();
        recordRepository.save(record5);
        ReflectionTestUtils.setField(record5, "createDate", LocalDateTime.of(2024, 3, 25, 0, 0));

        //when
        Pageable pageable = PageRequest.of(0, 8);
        Page<RecordRespDto> result = recordRepository.findAllByContentSearch(s, null,
                LocalDateTime.of(2024, 3, 1, 0, 0),
                LocalDateTime.of(2024, 3, 31, 0, 0),
                pageable);

        //then
        assertThat(result.get().collect(Collectors.toList()).size()).isEqualTo(2);
        assertThat(result.get().collect(Collectors.toList()).get(0).getCreateDate()).isEqualTo(LocalDateTime.of(2024, 3, 15, 23, 15));
        assertThat(result.get().collect(Collectors.toList()).get(0).getContent()).isEqualTo(record4.getContent());
    }

    @Test
    void 특정_검색어와_특정_날짜를_포함하는_피드백_조회_테스트() {
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
        ReflectionTestUtils.setField(record4, "createDate", LocalDateTime.of(2024, 3, 15, 23, 15));

        Record record5 = Record.builder()
                .teacher(t3)
                .student(s)
                .status(RecordStatus.DELETED)
                .content("ABCDEEEE")
                .build();
        recordRepository.save(record5);
        ReflectionTestUtils.setField(record5, "createDate", LocalDateTime.of(2024, 3, 25, 0, 0));

        //when
        Pageable pageable = PageRequest.of(0, 8);
        Page<RecordRespDto> result = recordRepository.findAllByContentSearch(s, "파하",
                LocalDateTime.of(2024, 3, 1, 0, 0),
                LocalDateTime.of(2024, 3, 31, 0, 0),
                pageable);

        //then
        assertThat(result.get().collect(Collectors.toList()).size()).isEqualTo(1);
        assertThat(result.get().collect(Collectors.toList()).get(0).getContent()).isEqualTo(record4.getContent());
        assertThat(result.get().collect(Collectors.toList()).get(0).getCreateDate()).isEqualTo(record4.getCreateDate());
    }

    @Test
    void 선생님_이름과_특정_날짜를_포함하는_피드백_조회_테스트() {
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
                .content("줄리아1월피드백")
                .build();
        recordRepository.save(record1);
        ReflectionTestUtils.setField(record1, "createDate", LocalDateTime.of(2024, 1, 1, 0, 0));

        Record record2 = Record.builder()
                .teacher(t1)
                .student(s)
                .status(RecordStatus.PUBLISHED)
                .content("줄리아1월피드백")
                .build();
        recordRepository.save(record2);
        ReflectionTestUtils.setField(record2, "createDate", LocalDateTime.of(2024, 1, 15, 0, 0));

        Record record3 = Record.builder()
                .teacher(t2)
                .student(s)
                .status(RecordStatus.PUBLISHED)
                .content("안샘3월피드백")
                .build();
        recordRepository.save(record3);
        ReflectionTestUtils.setField(record3, "createDate", LocalDateTime.of(2024, 3, 1, 0, 0));

        Record record4 = Record.builder()
                .teacher(t1)
                .student(s)
                .status(RecordStatus.PUBLISHED)
                .content("줄리아4월피드백")
                .build();
        recordRepository.save(record4);
        ReflectionTestUtils.setField(record4, "createDate", LocalDateTime.of(2024, 4, 15, 23, 15));

        Record record5 = Record.builder()
                .teacher(t3)
                .student(s)
                .status(RecordStatus.PUBLISHED)
                .content("대박샘3월피드백")
                .build();
        recordRepository.save(record5);
        ReflectionTestUtils.setField(record5, "createDate", LocalDateTime.of(2024, 3, 25, 0, 0));

        //when
        Pageable pageable = PageRequest.of(0, 8);
        Page<RecordRespDto> resultJuliaJan = recordRepository.findAllByTeacherNameSearch(s, "줄리아",
                LocalDateTime.of(2024, 1, 1, 0, 0),
                LocalDateTime.of(2024, 1, 31, 0, 0),
                pageable);

        Page<RecordRespDto> resultJuliaJanToMay = recordRepository.findAllByTeacherNameSearch(s, "줄리아",
                LocalDateTime.of(2024, 1, 1, 0, 0),
                LocalDateTime.of(2024, 5, 31, 0, 0),
                pageable);

        //then
        assertThat(resultJuliaJan.get().collect(Collectors.toList()).size()).isEqualTo(2);
        assertThat(resultJuliaJan.get().collect(Collectors.toList()).get(0).getCreateDate()).isEqualTo(record2.getCreateDate());

        assertThat(resultJuliaJanToMay.get().collect(Collectors.toList()).size()).isEqualTo(3);
        assertThat(resultJuliaJanToMay.get().collect(Collectors.toList()).get(0).getCreateDate()).isEqualTo(record4.getCreateDate());
        assertThat(resultJuliaJanToMay.get().collect(Collectors.toList()).get(0).getContent()).isEqualTo(record4.getContent());

    }

    @Test
    void 선생님_이름을_포함하지_않고_날짜만_포함하는_피드백_필터링_테스트() {
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
                .content("줄리아1월피드백")
                .build();
        recordRepository.save(record1);
        ReflectionTestUtils.setField(record1, "createDate", LocalDateTime.of(2024, 1, 1, 0, 0));

        Record record2 = Record.builder()
                .teacher(t1)
                .student(s)
                .status(RecordStatus.PUBLISHED)
                .content("줄리아1월피드백")
                .build();
        recordRepository.save(record2);
        ReflectionTestUtils.setField(record2, "createDate", LocalDateTime.of(2024, 1, 15, 0, 0));

        Record record3 = Record.builder()
                .teacher(t2)
                .student(s)
                .status(RecordStatus.PUBLISHED)
                .content("안샘3월피드백")
                .build();
        recordRepository.save(record3);
        ReflectionTestUtils.setField(record3, "createDate", LocalDateTime.of(2024, 3, 1, 0, 0));

        Record record4 = Record.builder()
                .teacher(t1)
                .student(s)
                .status(RecordStatus.PUBLISHED)
                .content("줄리아4월피드백")
                .build();
        recordRepository.save(record4);
        ReflectionTestUtils.setField(record4, "createDate", LocalDateTime.of(2024, 4, 15, 23, 15));

        Record record5 = Record.builder()
                .teacher(t3)
                .student(s)
                .status(RecordStatus.PUBLISHED)
                .content("대박샘3월피드백")
                .build();
        recordRepository.save(record5);
        ReflectionTestUtils.setField(record5, "createDate", LocalDateTime.of(2024, 3, 25, 0, 0));

        //when
        Pageable pageable = PageRequest.of(0, 8);
        Page<RecordRespDto> resultJanToMay = recordRepository.findAllByTeacherNameSearch(s, null,
                LocalDateTime.of(2024, 1, 1, 0, 0),
                LocalDateTime.of(2024, 5, 31, 0, 0),
                pageable);

        //then
        assertThat(resultJanToMay.get().collect(Collectors.toList()).size()).isEqualTo(5);
        assertThat(resultJanToMay.get().collect(Collectors.toList()).get(0).getCreateDate()).isEqualTo(record4.getCreateDate());
        assertThat(resultJanToMay.get().collect(Collectors.toList()).get(4).getContent()).isEqualTo(record1.getContent());
    }
}