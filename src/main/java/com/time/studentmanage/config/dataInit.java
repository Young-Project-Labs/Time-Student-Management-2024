package com.time.studentmanage.config;

import com.time.studentmanage.domain.enums.GenderType;
import com.time.studentmanage.domain.enums.MemberType;
import com.time.studentmanage.domain.enums.Position;
import com.time.studentmanage.domain.member.Teacher;
import com.time.studentmanage.repository.teacher.TeacherRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@RequiredArgsConstructor

public class dataInit {
    private final TeacherRepository teacherRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @PostConstruct
    public void initTeacher() {
        String password = "1234";
        String encodePassword = passwordEncoder.encode(password);
        Teacher teacher1 = Teacher.builder()
                .name("대박샘").email("pjj@time.com").password(encodePassword).phoneNumber("010-3434-5678")
                .memberType(MemberType.TEACHER).position(Position.CHIEF).gender(GenderType.MALE)
                .build();
        Teacher teacher2 = Teacher.builder()
                .name("줄리아").email("julia@time.com").password(encodePassword).phoneNumber("010-1212-3456")
                .memberType(MemberType.TEACHER).position(Position.TEACHER).gender(GenderType.FEMALE)
                .build();
        Teacher teacher3 = Teacher.builder()
                .name("안샘").email("ahn@time.com").password(encodePassword).phoneNumber("010-4545-2342")
                .memberType(MemberType.TEACHER).position(Position.TEACHER).gender(GenderType.MALE)
                .build();
        Teacher teacher4 = Teacher.builder()
                .name("소피아").email("sp@time.com").password(encodePassword).phoneNumber("010-1414-5515")
                .memberType(MemberType.TEACHER).position(Position.TEACHER).gender(GenderType.FEMALE)
                .build();
        teacherRepository.save(teacher1);
        teacherRepository.save(teacher2);
        teacherRepository.save(teacher3);
        teacherRepository.save(teacher4);

    }
}
