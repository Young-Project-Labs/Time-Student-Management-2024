package com.time.studentmanage;

import com.time.studentmanage.domain.enums.GenderType;
import com.time.studentmanage.domain.enums.MemberType;
import com.time.studentmanage.domain.enums.Position;
import com.time.studentmanage.domain.member.Teacher;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;
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