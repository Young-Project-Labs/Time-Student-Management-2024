package com.time.studentmanage.config;

import com.time.studentmanage.domain.Address;
import com.time.studentmanage.domain.enums.*;
import com.time.studentmanage.domain.member.Parent;
import com.time.studentmanage.domain.member.Student;
import com.time.studentmanage.domain.member.Teacher;
import com.time.studentmanage.repository.ParentRepository;
import com.time.studentmanage.repository.StudentRepository;
import com.time.studentmanage.repository.TeacherRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class TestDataInit {

    private final StudentRepository studentRepository;
    private final TeacherRepository teacherRepository;
    private final ParentRepository parentRepository;

    @PostConstruct
    public void initStudent() {
        Student student1 = new Student("철수", "cs@time.com", "1234", "010-1111-2222", "용호초등학교", ClassType.ELEMENTARY, 1, MemberType.STUDENT, GenderType.MALE, new Address("반림동", "현대 아파트", "102-1201"), AttendanceStatus.Y);
        Student student2 = new Student("진진진", "jjj@time.com", "1234", "010-2222-3333", "용호초등학교", ClassType.ELEMENTARY, 6, MemberType.STUDENT, GenderType.FEMALE, new Address("반림동", "반림 아파트", "111-123"), AttendanceStatus.Y);
        Student student3 = new Student("노진구", "njk@time.com", "1234", "010-4444-5555", "용호중학교", ClassType.MIDDLE, 3, MemberType.STUDENT, GenderType.MALE, new Address("반림동", "반림 아파트", "111-456"), AttendanceStatus.Y);
        Student student4 = new Student("짱구", "jg@time.com", "1234", "010-6666-7777", "반림중학교", ClassType.MIDDLE, 2, MemberType.STUDENT, GenderType.FEMALE, new Address("반림동", "반림 아파트", "111-345"), AttendanceStatus.Y);
        Student student5 = new Student("뚱이", "star@time.com", "1234", "010-8888-9999", "용호고등학교", ClassType.HIGH, 3, MemberType.STUDENT, GenderType.MALE, new Address("반림동", "반림 아파트", "111-789"), AttendanceStatus.Y);
        studentRepository.save(student1);
        studentRepository.save(student2);
        studentRepository.save(student3);
        studentRepository.save(student4);
        studentRepository.save(student5);
    }

    @PostConstruct
    public void initTeacher() {
        Teacher teacher1 = Teacher.builder()
                .name("대박샘").userId("pjj@time.com").password("1234")
                .email("pjj@time.com").phoneNumber("010-3434-5678")
                .memberType(MemberType.TEACHER).position(Position.CHIEF).gender(GenderType.MALE)
                .build();
        Teacher teacher2 = Teacher.builder()
                .name("줄리아").userId("julia@time.com").password("1234")
                .email("julia@time.com").phoneNumber("010-1212-3456")
                .memberType(MemberType.TEACHER).position(Position.TEACHER).gender(GenderType.FEMALE)
                .build();
        Teacher teacher3 = Teacher.builder()
                .name("안샘").userId("ahn@time.com").password("1234")
                .email("ahn@time.com").phoneNumber("010-4545-2342")
                .memberType(MemberType.TEACHER).position(Position.TEACHER).gender(GenderType.MALE)
                .build();
        Teacher teacher4 = Teacher.builder()
                .name("소피아").userId("sp@time.com").password("1234")
                .email("sp@time.com").phoneNumber("010-1414-5515")
                .memberType(MemberType.TEACHER).position(Position.TEACHER).gender(GenderType.FEMALE)
                .build();
        teacherRepository.save(teacher1);
        teacherRepository.save(teacher2);
        teacherRepository.save(teacher3);
        teacherRepository.save(teacher4);

    }

    @PostConstruct
    public void initParent() {
        Parent parent = new Parent("철수엄마", "010-1234-4567", MemberType.PARENT, GenderType.FEMALE);
        Parent parent2 = new Parent("진진진엄마", "010-1234-1224", MemberType.PARENT, GenderType.FEMALE);
        Parent parent3 = new Parent("진진진아빠", "010-1234-1234", MemberType.PARENT, GenderType.MALE);
        Parent parent4 = new Parent("노진구엄마", "010-4567-1234", MemberType.PARENT, GenderType.FEMALE);
        Parent parent5 = new Parent("짱구엄마", "010-1234-4234", MemberType.PARENT, GenderType.FEMALE);
        Parent parent6 = new Parent("뚱이엄마", "010-1234-4525", MemberType.PARENT, GenderType.FEMALE);
        Parent parent7 = new Parent("뚱이아빠", "010-1234-4125", MemberType.PARENT, GenderType.MALE);
        parentRepository.save(parent);
        parentRepository.save(parent2);
        parentRepository.save(parent3);
        parentRepository.save(parent4);
        parentRepository.save(parent5);
        parentRepository.save(parent6);
        parentRepository.save(parent7);
    }

}
