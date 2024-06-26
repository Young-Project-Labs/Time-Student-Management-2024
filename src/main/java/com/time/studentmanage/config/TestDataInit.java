package com.time.studentmanage.config;

import com.time.studentmanage.domain.classroom.ClassRoom;
import com.time.studentmanage.domain.enums.*;
import com.time.studentmanage.domain.member.Address;
import com.time.studentmanage.domain.member.Student;
import com.time.studentmanage.domain.member.Teacher;
import com.time.studentmanage.domain.record.Record;
import com.time.studentmanage.repository.classroom.ClassRoomRepository;
import com.time.studentmanage.repository.record.RecordRepository;
import com.time.studentmanage.repository.student.StudentRepository;
import com.time.studentmanage.repository.teacher.TeacherRepository;
import com.time.studentmanage.service.StudentService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@RequiredArgsConstructor
public class TestDataInit {

    private final StudentService studentService;
    private final StudentRepository studentRepository;
    private final TeacherRepository teacherRepository;
    private final RecordRepository recordRepository;
    private final ClassRoomRepository classRoomRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @PostConstruct
    @Transactional
    public void initClassRoom() {
        String password = "1234";
        String encodePassword = passwordEncoder.encode(password);
        Student student1 = Student.builder()
                .name("홍길동")
                .userId("hgd1234").password(encodePassword)
                .classType(ClassType.ELEMENTARY)
                .grade(2)
                .memberType(MemberType.STUDENT).gender(GenderType.MALE)
                .attendanceStatus(AttendanceStatus.Y)
                .build();

        Student student2 = Student.builder()
                .name("노진구")
                .userId("njk1234").password(encodePassword)
                .email("test3@naver.com")
                .parentName("노진구어머니").parentPhoneNumber("010-3322-3344")
                .phoneNumber("010-4444-5555").schoolName("용호중학교")
                .classType(ClassType.MIDDLE).grade(3)
                .memberType(MemberType.STUDENT).gender(GenderType.FEMALE)
                .address(new Address("반림동", "현대 아파트", "102-342"))
                .attendanceStatus(AttendanceStatus.Y)
                .build();

        Student student3 = Student.builder()
                .name("짱구")
                .userId("jg1234").password(encodePassword)
                .email("test4@naver.com")
                .parentName("짱구어머니").parentPhoneNumber("010-1111-3344")
                .phoneNumber("010-1111-2222").schoolName("반림중학교")
                .classType(ClassType.MIDDLE).grade(2)
                .memberType(MemberType.STUDENT).gender(GenderType.FEMALE)
                .address(new Address("반림동", "현대 아파트", "102-1201"))
                .attendanceStatus(AttendanceStatus.Y)
                .build();

        Student studentPS1 = studentRepository.save(student1);
        Student studentPS2 = studentRepository.save(student2);
        Student studentPS3 = studentRepository.save(student3);

        Teacher teacher1 = Teacher.builder()
                .name("한석봉").password(encodePassword)
                .email("hsb@time.com").phoneNumber("010-1212-3332")
                .memberType(MemberType.TEACHER).position(Position.TEACHER).gender(GenderType.MALE)
                .build();


        teacherRepository.save(teacher1);

        ClassRoom classRoom = ClassRoom.builder()
                .name("길동반")
                .classInfo("월, 수, 금")
                .teacher(teacher1)
                .classType(ClassType.ELEMENTARY)
                .build();

        classRoomRepository.save(classRoom);

        studentService.connectClassRoom(studentPS1.getId(), classRoom);
        studentService.connectClassRoom(studentPS2.getId(), classRoom);
        studentService.connectClassRoom(studentPS3.getId(), classRoom);


        for (int i = 1; i < 50; i++) {
            if (i % 2 == 0) {
                ClassRoom classRoomEven = ClassRoom.builder()
                        .name("짝수 반" + i)
                        .classInfo("월, 수, 금")
                        .teacher(teacher1)
                        .classType(ClassType.ELEMENTARY)
                        .build();

                classRoomRepository.save(classRoomEven);
            } else {
                ClassRoom classRoomOdd = ClassRoom.builder()
                        .name("홀수 반" + i)
                        .classInfo("화,목")
                        .teacher(teacher1)
                        .classType(ClassType.ELEMENTARY)
                        .build();

                classRoomRepository.save(classRoomOdd);
            }


        }
    }

    @PostConstruct
    @Transactional
    public void initRecord() {
        String password = "1234";
        String encodePassword = passwordEncoder.encode(password);
        Student student1 = Student.builder()
                .name("짱구")
                .userId("pyjs2291").password(encodePassword)
                .email("pyjs2291@naver.com")
                .phoneNumber("010-1111-3311").schoolName("용호초등학교")
                .classType(ClassType.ELEMENTARY).grade(2)
                .memberType(MemberType.STUDENT).gender(GenderType.FEMALE)
                .address(new Address("반림동", "현대 아파트", "102-301"))
                .attendanceStatus(AttendanceStatus.Y)
                .build();

        Teacher teacher1 = Teacher.builder()
                .name("고길동").email("kkd1234").password(encodePassword)
                .email("ggd@time.com").phoneNumber("010-1212-3332")
                .memberType(MemberType.TEACHER).position(Position.TEACHER).gender(GenderType.MALE)
                .build();

        studentRepository.save(student1);
        teacherRepository.save(teacher1);

        Record record1 = Record.builder()
                .student(student1)
                .teacher(teacher1)
                .title("제목인건가")
                .content("1번 피드백 입니다.")
                .view(0)
                .status(RecordStatus.PUBLISHED)
                .build();

        Record record2 = Record.builder()
                .student(student1)
                .teacher(teacher1)
                .title("제목인건가")
                .content("2번 피드백 입니다.")
                .view(0)
                .status(RecordStatus.PUBLISHED)
                .build();

        Record record3 = Record.builder()
                .student(student1)
                .teacher(teacher1)
                .title("제목인건가")
                .content("3번 피드백 입니다.")
                .view(0)
                .status(RecordStatus.PUBLISHED)
                .build();

        Record record4 = Record.builder()
                .student(student1)
                .teacher(teacher1)
                .title("제목인건가")
                .content("4번 피드백 입니다.")
                .view(0)
                .status(RecordStatus.PUBLISHED)
                .build();

        Record record5 = Record.builder()
                .student(student1)
                .teacher(teacher1)
                .title("제목인건가")
                .content("5번 피드백 입니다.")
                .view(0)
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
        for (int i = 0; i < 200; i++) {
            LocalDateTime randomCreateDate = LocalDateTime.now().minusDays((long) (Math.random() * 365));

            Record record = Record.builder()
                    .student(student)
                    .teacher(teacher)
                    .title((i + 1) + "번 피드백 제목 입니다.")
                    .content((i + 1) + "번 테스트 피드백 입니다.")
                    .status(RecordStatus.PUBLISHED)
                    .view(0)
                    .build();
            record.addStudent(student);
            record.addTeacher(teacher);

//            record.setCreateDate(randomCreateDate);
//            record.setModifiedDate(randomCreateDate);

            recordRepository.save(record);
            record.getCreateDate();
        }
    }

    @PostConstruct
    public void initStudent() {
        for (int i = 0; i < 30; i++) {
            Student studentA = Student.builder()
                    .name("A초등학교 학생" + (i + 1))
                    .attendanceStatus(AttendanceStatus.Y)
                    .classType(ClassType.ELEMENTARY)
                    .schoolName("A초등학교")
                    .parentName("A초등학교 학생" + (i + 1) + "부모님")
                    .build();
            Student studentB = Student.builder()
                    .name("B중학교 학생" + (i + 1))
                    .attendanceStatus(AttendanceStatus.Y)
                    .classType(ClassType.MIDDLE)
                    .schoolName("B중학교")
                    .parentName("B중학교 학생" + (i + 1) + "부모님")
                    .build();
            Student studentC = Student.builder()
                    .name("C고등학교 학생" + (i + 1))
                    .attendanceStatus(AttendanceStatus.Y)
                    .classType(ClassType.HIGH)
                    .schoolName("C고등학교")
                    .parentName("C고등학교 학생" + (i + 1) + "부모님")
                    .build();
            studentRepository.save(studentA);
            studentRepository.save(studentB);
            studentRepository.save(studentC);
        }


        String password = "1234";
        String encodePassword = passwordEncoder.encode(password);
        Student student1 = Student.builder()
                .name("김영재")
                .userId("cs123456").password(encodePassword)
                .email("youngjae459@naver.com")
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
                .email("test2@naver.com")
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


        Student student5 = Student.builder()
                .name("뚱이")
                .userId("star1234").password(encodePassword)
                .email("test5@naver.com")
                .parentName("뚱이어머니").parentPhoneNumber("010-2342-3344")
                .phoneNumber("010-1111-2222").schoolName("용호고등학교")
                .classType(ClassType.HIGH).grade(3)
                .memberType(MemberType.STUDENT).gender(GenderType.FEMALE)
                .address(new Address("반림동", "현대 아파트", "102-1111"))
                .attendanceStatus(AttendanceStatus.Y)
                .build();
        studentRepository.save(student1);
        studentRepository.save(student2);

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

    @PostConstruct
    public void createDummyTeacher() {
        String password = "1234";
        String encodePassword = passwordEncoder.encode(password);
        for (int i = 0; i < 100; i++) {
            Teacher teacher = Teacher.builder()
                    .name("테스트선생" + i)
                    .password(encodePassword)
                    .phoneNumber("010-1111-1111")
                    .email("test" + i + "@naver.com")
                    .position(Position.TEACHER)
                    .memberType(MemberType.TEACHER)
                    .gender(GenderType.MALE)
                    .build();
            teacherRepository.save(teacher);
        }
    }


}
