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
        Student student1 = Student.builder()
                .name("철수")
                .userId("cs@time.com").password("1234")
                .phoneNumber("010-1111-2222").schoolName("용호초등학교")
                .classType(ClassType.ELEMENTARY).grade(1)
                .memberType(MemberType.STUDENT).gender(GenderType.MALE)
                .address(new Address("반림동", "현대 아파트", "102-1201"))
                .attendanceStatus(AttendanceStatus.Y)
                .build();

        Student student2 = Student.builder()
                .name("진진진")
                .userId("jjj@time.com")
                .password("1234")
                .phoneNumber("010-2222-3333")
                .schoolName("용호초등학교")
                .classType(ClassType.ELEMENTARY)
                .grade(6)
                .memberType(MemberType.STUDENT)
                .gender(GenderType.MALE)
                .address(new Address("반림동", "현대 아파트", "111-1201"))
                .attendanceStatus(AttendanceStatus.Y)
                .build();

        Student student3 = Student.builder()
                .name("노진구")
                .userId("njk@time.com").password("1234")
                .phoneNumber("010-4444-5555").schoolName("용호중학교")
                .classType(ClassType.MIDDLE).grade(3)
                .memberType(MemberType.STUDENT).gender(GenderType.MALE)
                .address(new Address("반림동", "현대 아파트", "102-342"))
                .attendanceStatus(AttendanceStatus.Y)
                .build();

        Student student4 = Student.builder()
                .name("짱구")
                .userId("jg@time.com").password("1234")
                .phoneNumber("010-1111-2222").schoolName("반림중학교")
                .classType(ClassType.MIDDLE).grade(2)
                .memberType(MemberType.STUDENT).gender(GenderType.MALE)
                .address(new Address("반림동", "현대 아파트", "102-1201"))
                .attendanceStatus(AttendanceStatus.Y)
                .build();

        Student student5 = Student.builder()
                .name("뚱이")
                .userId("star@time.com").password("1234")
                .phoneNumber("010-1111-2222").schoolName("용호고등학교")
                .classType(ClassType.HIGH).grade(3)
                .memberType(MemberType.STUDENT).gender(GenderType.MALE)
                .address(new Address("반림동", "현대 아파트", "102-1111"))
                .attendanceStatus(AttendanceStatus.Y)
                .build();
        studentRepository.save(student1);
        studentRepository.save(student2);
        studentRepository.save(student3);
        studentRepository.save(student4);
        studentRepository.save(student5);
    }

    @PostConstruct
    public void initTeacher() {
        Teacher teacher1 = new Teacher("대박샘", "pjj@time.com", "1234", "010-3434-5678", MemberType.TEACHER, Position.CHIEF, "pjj@time.com", GenderType.MALE);
        Teacher teacher2 = new Teacher("줄리아", "julia@time.com", "1234", "010-1212-3456", MemberType.TEACHER, Position.TEACHER, "julia@time.com", GenderType.FEMALE);
        Teacher teacher3 = new Teacher("안샘", "ahn@time.com", "1234", "010-4545-2342", MemberType.TEACHER, Position.TEACHER, "ahn@time.com", GenderType.MALE);
        Teacher teacher4 = new Teacher("소피아", "sp@time.com", "1234", "010-1414-5515", MemberType.TEACHER, Position.TEACHER, "sp@time.com", GenderType.FEMALE);
        teacherRepository.save(teacher1);
        teacherRepository.save(teacher2);
        teacherRepository.save(teacher3);
        teacherRepository.save(teacher4);

    }

    @PostConstruct
    public void initParent() {
        Parent parent1 = Parent.builder()
                .name("철수엄마").phoneNumber("010-1234-4567")
                .memberType(MemberType.PARENT).gender(GenderType.FEMALE)
                .build();

        Parent parent2 = Parent.builder()
                .name("진진진엄마").phoneNumber("010-1234-1224")
                .memberType(MemberType.PARENT).gender(GenderType.FEMALE)
                .build();

        Parent parent3 = Parent.builder()
                .name("진진진아빠").phoneNumber("010-1234-1234")
                .memberType(MemberType.PARENT).gender(GenderType.MALE)
                .build();

        Parent parent4 = Parent.builder()
                .name("노진구엄마").phoneNumber("010-4567-1234")
                .memberType(MemberType.PARENT).gender(GenderType.FEMALE)
                .build();

        Parent parent5 = Parent.builder()
                .name("짱구엄마").phoneNumber("010-1234-4234")
                .memberType(MemberType.PARENT).gender(GenderType.FEMALE)
                .build();

        Parent parent6 = Parent.builder()
                .name("뚱이엄마").phoneNumber("010-1234-4525")
                .memberType(MemberType.PARENT).gender(GenderType.FEMALE)
                .build();

        Parent parent7 = Parent.builder()
                .name("뚱이아빠").phoneNumber("010-1234-4125")
                .memberType(MemberType.PARENT).gender(GenderType.MALE)
                .build();

        parentRepository.save(parent1);
        parentRepository.save(parent2);
        parentRepository.save(parent3);
        parentRepository.save(parent4);
        parentRepository.save(parent5);
        parentRepository.save(parent6);
        parentRepository.save(parent7);
    }

}
