package com.time.studentmanage.repository;

import com.time.studentmanage.domain.enums.GenderType;
import com.time.studentmanage.domain.enums.MemberType;
import com.time.studentmanage.domain.enums.Position;
import com.time.studentmanage.domain.member.Teacher;
import com.time.studentmanage.repository.TeacherRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@SpringBootTest
class TeacherRepositoryTest {
    @Autowired
    TeacherRepository teacherRepository;
    @Test
    void changeTest() {
        Teacher teacher1 = new Teacher("대박샘", "pjj@time.com", "1234", "010-3434-5678", MemberType.TEACHER, Position.CHIEF, "pjj@time.com", GenderType.MALE);
        teacherRepository.save((teacher1));

        Assertions.assertThat(teacherRepository.findAll().size()).isEqualTo(1);

    }
}