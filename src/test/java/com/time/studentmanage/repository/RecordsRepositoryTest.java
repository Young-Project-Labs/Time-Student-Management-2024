package com.time.studentmanage.repository;

import com.time.studentmanage.domain.Address;
import com.time.studentmanage.domain.Records;
import com.time.studentmanage.domain.enums.*;
import com.time.studentmanage.domain.member.Student;
import com.time.studentmanage.domain.member.Teacher;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@DataJpaTest
@Transactional
class RecordsRepositoryTest {

    @Autowired
    RecordsRepository recordsRepository;

    @Test
    @Transactional(readOnly = true)
    void 피드백_생성_조회_테스트() {
        // given
        Student s = new Student("철수", "cs@time.com", "1234", "010-1111-2222", "용호초등학교", ClassType.ELEMENTARY, 1, MemberType.STUDENT, GenderType.MALE, new Address("반림동", "현대 아파트", "102-1201"), AttendanceStatus.Y);
        Teacher t = new Teacher("줄리아", "julia@time.com", "1234", "010-1212-3456", MemberType.TEACHER, Position.TEACHER, "julia@time.com", GenderType.FEMALE);

        // when
        Records record = new Records(t, s, "철수의 문법 수준이 높습니다. 테스트 후 초등 고학년 문법반으로 올려도 될 것 같습니다.");

        Records savedRecord = recordsRepository.save(record);
        Records findRecord = recordsRepository.findById(savedRecord.getId()).get();

        // then
        assertThat(findRecord.getContent()).isEqualTo(savedRecord.getContent());
    }

    @Test
    void 피드백_수정_테스트() {
        // given
        Student student = createStudent();
        Teacher teacher = createTeacher();
        
        Records records = new Records(teacher, student, "철수의 문법 수준이 높습니다. 테스트 후 초등 고학년 문법반으로 올려도 될 것 같습니다.");
        records.addStudent(student);
        records.addTeacher(teacher);

        // when
        Records savedRecord = recordsRepository.save(records);
        Records findRecord = recordsRepository.findById(savedRecord.getId()).get();
        findRecord.setContent("철수가 문법 수준은 높으나 독해에 있어서 보충이 필요해 보입니다.");
        recordsRepository.save(findRecord);

        //then
        Records updatedRecord = recordsRepository.findById(findRecord.getId()).get();
        assertThat(updatedRecord.getContent()).isEqualTo(findRecord.getContent());
    }

    private Student createStudent() {
        Student student = new Student("철수", "cs@time.com", "1234", "010-1111-2222", "용호초등학교", ClassType.ELEMENTARY, 1, MemberType.STUDENT, GenderType.MALE, new Address("반림동", "현대 아파트", "102-1201"), AttendanceStatus.Y);
        return student;
    }

    private Teacher createTeacher() {
        Teacher teacher = new Teacher("줄리아", "julia@time.com", "1234", "010-1212-3456", MemberType.TEACHER, Position.TEACHER, "julia@time.com", GenderType.FEMALE);
        return teacher;
    }
}