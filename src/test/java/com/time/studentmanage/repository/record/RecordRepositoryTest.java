package com.time.studentmanage.repository.record;

import com.time.studentmanage.domain.dto.record.RecordRespDto;
import com.time.studentmanage.domain.dto.record.RecordSearchDto;
import com.time.studentmanage.domain.dto.record.RecordSearchReqCondition;
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
import java.util.Random;
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
    void 검색타입과_검색내용을_기반으로_검색() {
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
                .name("소피아샘")
                .build();

        studentRepository.save(s);
        teacherRepository.save(t1);
        teacherRepository.save(t2);
        teacherRepository.save(t3);

        // 줄리아 테스트 피드백 더미 저장
        for (int i = 0; i < 20; i++) {
            Record record = Record.builder()
                    .teacher(t1)
                    .student(s)
                    .status(RecordStatus.PUBLISHED)
                    .content("줄리아피드백" + (i + 1))
                    .build();
            recordRepository.save(record);
            ReflectionTestUtils.setField(record, "createDate", LocalDateTime.of(2024, 1, 23, 0, 0));
        }

        // 안샘 테스트 피드백 더미 저장
        for (int i = 0; i < 20; i++) {
            Record record = Record.builder()
                    .teacher(t2)
                    .student(s)
                    .status(RecordStatus.PUBLISHED)
                    .content("안샘피드백" + (i + 1))
                    .build();
            recordRepository.save(record);
            ReflectionTestUtils.setField(record, "createDate", LocalDateTime.of(2024, Math.toIntExact(new Random().nextInt(11)) + 1, Math.toIntExact(new Random().nextInt(20)) + 1, 0, 0));
        }

        // 소피아샘 테스트 피드백 더미 저장
        for (int i = 0; i < 20; i++) {
            Record record = Record.builder()
                    .teacher(t3)
                    .student(s)
                    .status(RecordStatus.PUBLISHED)
                    .content("소피아샘피드백" + (i + 1))
                    .build();
            recordRepository.save(record);
            ReflectionTestUtils.setField(record, "createDate", LocalDateTime.of(2024, Math.toIntExact(new Random().nextInt(11)) + 1, Math.toIntExact(new Random().nextInt(20)) + 1, 0, 0));
        }

        //when

        Pageable pageable = PageRequest.of(0, 8);
        RecordSearchReqCondition searchCondition = new RecordSearchReqCondition(s, SearchType.CONTENT, "줄리아피드백", null, null, pageable);

        Page<RecordRespDto> pageResult = recordRepository.findAllBySearchEngine(searchCondition);

        //then
        log.info("pageResult={}", pageResult);
        assertThat(pageResult.get().collect(Collectors.toList()).size()).isEqualTo(8);

        Page<RecordRespDto> pagingResultNameSearch = recordRepository.findAllBySearchEngine(new RecordSearchReqCondition(s, SearchType.TEACHER_NAME, "안샘", null, null, PageRequest.of(2,8)));
        assertThat(pagingResultNameSearch.get().collect(Collectors.toList()).size()).isEqualTo(4);
        assertThat(pagingResultNameSearch.get().collect(Collectors.toList()).get(3).getTeacherName()).isEqualTo("안샘");
        assertThat(pagingResultNameSearch.get().collect(Collectors.toList()).get(0).getCreateDate()).isAfter(pagingResultNameSearch.get().collect(Collectors.toList()).get(3).getCreateDate());

    }
}