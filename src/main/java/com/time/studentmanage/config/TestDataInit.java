package com.time.studentmanage.config;

import com.time.studentmanage.domain.Address;
import com.time.studentmanage.domain.Record;
import com.time.studentmanage.domain.enums.*;
import com.time.studentmanage.domain.member.Student;
import com.time.studentmanage.domain.member.Teacher;
import com.time.studentmanage.repository.RecordRepository;
import com.time.studentmanage.repository.StudentRepository;
import com.time.studentmanage.repository.TeacherRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
public class TestDataInit {

    private final StudentRepository studentRepository;
    private final TeacherRepository teacherRepository;
    private final RecordRepository recordRepository;

    @PostConstruct
    @Transactional
    public void initRecord() {
        Student student1 = Student.builder()
                .name("도라미")
                .userId("drm@time.com").password("1234")
                .phoneNumber("010-1111-3311").schoolName("용호초등학교")
                .classType(ClassType.ELEMENTARY).grade(2)
                .memberType(MemberType.STUDENT).gender(GenderType.FEMALE)
                .address(new Address("반림동", "현대 아파트", "102-301"))
                .attendanceStatus(AttendanceStatus.Y)
                .build();

        Teacher teacher1 = Teacher.builder()
                .name("고길동").userId("kkd@time.com").password("1234")
                .email("pjj@time.com").phoneNumber("010-1212-3332")
                .memberType(MemberType.TEACHER).position(Position.TEACHER).gender(GenderType.MALE)
                .build();

        studentRepository.save(student1);
        teacherRepository.save(teacher1);

        Record record1 = Record.builder()
                .student(student1)
                .teacher(teacher1)
                .content("1번 피드백 입니다.")
                .status(RecordStatus.PUBLISHED)
                .build();

        Record record2 = Record.builder()
                .student(student1)
                .teacher(teacher1)
                .content("2번 피드백 입니다.")
                .status(RecordStatus.PUBLISHED)
                .build();

        Record record3 = Record.builder()
                .student(student1)
                .teacher(teacher1)
                .content("3번 피드백 입니다.")
                .status(RecordStatus.PUBLISHED)
                .build();

        Record record4 = Record.builder()
                .student(student1)
                .teacher(teacher1)
                .content("4번 피드백 입니다.")
                .status(RecordStatus.PUBLISHED)
                .build();

        Record record5 = Record.builder()
                .student(student1)
                .teacher(teacher1)
                .content("5번 피드백 입니다.")
                .status(RecordStatus.PUBLISHED)
                .build();

        record1.addStudent(student1);
        record1.addTeacher(teacher1);

        record2.addStudent(student1);
        record2.addTeacher(teacher1);

        record3.addStudent(student1);
        record3.addTeacher(teacher1);

        record4.addStudent(student1);
        record4.addTeacher(teacher1);

        record5.addStudent(student1);
        record5.addTeacher(teacher1);


        recordRepository.save(record1);
        recordRepository.save(record2);
        recordRepository.save(record3);
        recordRepository.save(record4);
        recordRepository.save(record5);

    }

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


}
