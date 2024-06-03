package com.time.studentmanage.repository;

import com.time.studentmanage.domain.enums.GenderType;
import com.time.studentmanage.domain.enums.MemberType;
import com.time.studentmanage.domain.enums.Position;
import com.time.studentmanage.domain.member.Teacher;
import com.time.studentmanage.repository.teacher.TeacherRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
@Slf4j
@DataJpaTest
@TestMethodOrder(MethodOrderer.MethodName.class)
class TeacherRepositoryTest {
    @Autowired
    TeacherRepository teacherRepository;

    @Test
    void 선생님_회원_가입_테스트() {
        //given
        Teacher teacher1 = createTeacher();

        //when
        teacherRepository.save(teacher1);

        //then
        assertThat(teacherRepository.findAll().size()).isEqualTo(1);

    }

    @Test
    void 선생님_목록_테스트() {
        //given
        Teacher teacher1 = createTeacher();
        Teacher teacher2 = createTeacher();

        teacherRepository.save(teacher1);
        teacherRepository.save(teacher2);
        //when
        List<Teacher> teacherList = teacherRepository.findAll();
        //then
        assertThat(teacherList).hasSize(2);

    }

    private static Teacher createTeacher() {
        Teacher teacher = Teacher.builder()
                .name("대박샘")
                .password("1234")
                .phoneNumber("010-3434-5678")
                .memberType(MemberType.TEACHER)
                .position(Position.CHIEF)
                .email("pjj@time.com")
                .gender(GenderType.FEMALE)
                .build();
        return teacher;
    }
}