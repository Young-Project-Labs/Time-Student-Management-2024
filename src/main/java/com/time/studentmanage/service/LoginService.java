package com.time.studentmanage.service;

import com.time.studentmanage.domain.enums.MemberType;
import com.time.studentmanage.domain.member.Student;
import com.time.studentmanage.domain.member.Teacher;
import com.time.studentmanage.exception.DataNotFoundException;
import com.time.studentmanage.repository.StudentRepository;
import com.time.studentmanage.repository.TeacherRepository;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class LoginService {
    private final StudentRepository studentRepository;
    private final TeacherRepository teacherRepository;


    public Optional<?> login(String loginId, String password, MemberType memberType) {
        if (memberType == MemberType.STUDENT) {
            return studentRepository.findByUserIdAndPassword(loginId, password);
        }

        if (memberType == MemberType.TEACHER) {
            return teacherRepository.findByUserIdAndPassword(loginId, password);
        }

        return Optional.empty();
    }
}
