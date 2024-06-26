package com.time.studentmanage.service;

import com.time.studentmanage.domain.member.Student;
import com.time.studentmanage.domain.member.Teacher;
import com.time.studentmanage.repository.student.StudentRepository;
import com.time.studentmanage.repository.teacher.TeacherRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class LoginService {
    private final StudentRepository studentRepository;
    private final TeacherRepository teacherRepository;
    private final BCryptPasswordEncoder pwEncoder;


    public Optional<?> login(String loginId, String password) {
        /**
         * memberType = STUDENT
         */
        if (!loginId.contains("@time.com")) {
            log.info("학생 로그인");
            Optional<Student> studentOP = studentRepository.findByUserId(loginId);
            //ID에 맞는 학생이 없는 경우
            if (!studentOP.isPresent()) {
                return Optional.empty();
            }

            //패스워드 불일치
            if (!pwEncoder.matches(password, studentOP.get().getPassword())) {
                return Optional.empty();
            }

            //패스워드 일치
            return studentOP;
        }

        /**
         * memberType = TEACHER
         * userID@time.com
         */
        if (loginId.contains("@time.com")) {
            log.info("선생 로그인");
            Optional<Teacher> teacherOP = teacherRepository.findByEmail(loginId);
            //ID에 맞는 선생 없는 경우
            if (!teacherOP.isPresent()) {
                return Optional.empty();
            }

            //패스워드 불일치
            if (!pwEncoder.matches(password, teacherOP.get().getPassword())) {
                return Optional.empty();
            }

            //패스워드 일치
            return teacherOP;
        }

        // 멤버 타입이 없는 경우
        return Optional.empty();

    }
}
