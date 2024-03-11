package com.time.studentmanage.repository;

import com.time.studentmanage.domain.Address;
import com.time.studentmanage.domain.enums.*;
import com.time.studentmanage.domain.member.Student;
import com.time.studentmanage.domain.member.Teacher;
import com.time.studentmanage.repository.TeacherRepository;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Slf4j
@DataJpaTest
@TestMethodOrder(MethodOrderer.MethodName.class)
class TeacherRepositoryTest {
    @Autowired
    TeacherRepository teacherRepository;
    @Test
    //TODO: 빌더 패턴 적용 필요
    void 선생님_회원_가입_테스트() {
        //given
        Teacher teacher1 = createTeacher();

        //when
        teacherRepository.save(teacher1);

        //then
        Assertions.assertThat(teacherRepository.findAll().size()).isEqualTo(1);

    }

    private static Teacher createTeacher() {
        Teacher teacher1 = new Teacher("대박샘", "pjj@time.com", "1234", "010-3434-5678", MemberType.TEACHER, Position.CHIEF, "pjj@time.com", GenderType.MALE);
        return teacher1;
    }
}