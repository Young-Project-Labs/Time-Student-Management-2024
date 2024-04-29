package com.time.studentmanage.config;

import com.time.studentmanage.domain.enums.*;
import com.time.studentmanage.domain.member.Address;
import com.time.studentmanage.domain.member.Student;
import com.time.studentmanage.domain.member.Teacher;
import com.time.studentmanage.domain.record.Record;
import com.time.studentmanage.repository.record.RecordRepository;
import com.time.studentmanage.repository.student.StudentRepository;
import com.time.studentmanage.repository.teacher.TeacherRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
public class TestDataInit {

    private final StudentRepository studentRepository;
    private final TeacherRepository teacherRepository;
    private final RecordRepository recordRepository;
    private final BCryptPasswordEncoder passwordEncoder;


    @PostConstruct
    @Transactional
    public void initRecord() {
        String password = "1234";
        String encodePassword = passwordEncoder.encode(password);
        Student student1 = Student.builder()
                .name("도라미")
                .userId("drm@time.com").password(encodePassword)
                .phoneNumber("010-1111-3311").schoolName("용호초등학교")
                .classType(ClassType.ELEMENTARY).grade(2)
                .memberType(MemberType.STUDENT).gender(GenderType.FEMALE)
                .address(new Address("반림동", "현대 아파트", "102-301"))
                .attendanceStatus(AttendanceStatus.Y)
                .build();

        Teacher teacher1 = Teacher.builder()
                .name("고길동").email("kkd@time.com").password(encodePassword)
                .email("ggd@time.com").phoneNumber("010-1212-3332")
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

        createDummyRecordData(student1, teacher1);
    }

    public void createDummyRecordData(Student student, Teacher teacher) {
        for (int i = 0; i < 300; i++) {
            Record record = Record.builder()
                    .student(student)
                    .teacher(teacher)
                    .content((i + 1) + "번 테스트 피드백 입니다.")
                    .status(RecordStatus.PUBLISHED)
                    .build();
            record.addStudent(student);
            record.addTeacher(teacher);

            recordRepository.save(record);
        }
    }

    @PostConstruct
    public void initStudent() {
        String password = "1234";
        String encodePassword = passwordEncoder.encode(password);
        Student student1 = Student.builder()
                .name("철수")
                .userId("cs123456").password(encodePassword)
                .parentName("철수어머니").parentPhoneNumber("010-2222-3333")
                .phoneNumber("010-1111-2222").schoolName("용호초등학교")
                .classType(ClassType.ELEMENTARY).grade(1)
                .memberType(MemberType.STUDENT).gender(GenderType.MALE)
                .address(new Address("반림동", "현대 아파트", "102-1201"))
                .attendanceStatus(AttendanceStatus.Y)
                .build();

        Student student2 = Student.builder()
                .name("진진진")
                .userId("jjj123123").password(encodePassword)
                .parentName("진진진아버님").parentPhoneNumber("010-2222-3344")
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
                .userId("njk1234").password(encodePassword)
                .parentName("노진구어머니").parentPhoneNumber("010-3322-3344")
                .phoneNumber("010-4444-5555").schoolName("용호중학교")
                .classType(ClassType.MIDDLE).grade(3)
                .memberType(MemberType.STUDENT).gender(GenderType.FEMALE)
                .address(new Address("반림동", "현대 아파트", "102-342"))
                .attendanceStatus(AttendanceStatus.Y)
                .build();

        Student student4 = Student.builder()
                .name("짱구")
                .userId("jg1234").password(encodePassword)
                .parentName("짱구어머니").parentPhoneNumber("010-1111-3344")
                .phoneNumber("010-1111-2222").schoolName("반림중학교")
                .classType(ClassType.MIDDLE).grade(2)
                .memberType(MemberType.STUDENT).gender(GenderType.FEMALE)
                .address(new Address("반림동", "현대 아파트", "102-1201"))
                .attendanceStatus(AttendanceStatus.Y)
                .build();

        Student student5 = Student.builder()
                .name("뚱이")
                .userId("star@time.com").password(encodePassword)
                .parentName("뚱이어머니").parentPhoneNumber("010-2342-3344")
                .phoneNumber("010-1111-2222").schoolName("용호고등학교")
                .classType(ClassType.HIGH).grade(3)
                .memberType(MemberType.STUDENT).gender(GenderType.FEMALE)
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
