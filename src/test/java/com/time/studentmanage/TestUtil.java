package com.time.studentmanage;

import com.time.studentmanage.domain.Address;
import com.time.studentmanage.domain.Answer;
import com.time.studentmanage.domain.Records;
import com.time.studentmanage.domain.enums.*;
import com.time.studentmanage.domain.member.Parent;
import com.time.studentmanage.domain.member.Student;
import com.time.studentmanage.domain.member.Teacher;
import com.time.studentmanage.dto.StudentSaveReqDto;
import com.time.studentmanage.dto.StudentUpdateReqDto;

import java.util.ArrayList;
import java.util.List;

public class TestUtil {
    public static Records createRecord(Teacher teacher, Student student) {
        return Records.builder()
                .teacher(teacher)
                .student(student)
                .content("문법 수준이 높습니다. 테스트 후 초등 고학년 문법반으로 올려도 될 것 같습니다.")
                .build();
    }

    public static Student createStudent() {
        Student student = Student.builder()
                .name("철수")
                .userId("cs@time.com").password("1234")
                .phoneNumber("010-1111-2222").schoolName("용호초등학교")
                .classType(ClassType.ELEMENTARY).grade(1)
                .memberType(MemberType.STUDENT).gender(GenderType.MALE)
                .address(new Address("반림동", "현대 아파트", "102-1201"))
                .attendanceStatus(AttendanceStatus.Y)
                .build();
        return student;
    }

    public static List<Student> createManyStudent() {
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
                .userId("jjj@time.com").password("1234")
                .phoneNumber("010-2222-3333").schoolName("용호초등학교")
                .classType(ClassType.ELEMENTARY).grade(6)
                .memberType(MemberType.STUDENT).gender(GenderType.MALE)
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


        List<Student> studentList = new ArrayList<>();
        studentList.add(student1);
        studentList.add(student2);
        studentList.add(student3);
        studentList.add(student4);
        studentList.add(student5);
        return studentList;
    }

    public static Parent createParent() {
        //given
        Student student = Student.builder()
                .name("철수")
                .userId("cs@time.com").password("1234")
                .phoneNumber("010-1111-2222").schoolName("용호초등학교")
                .classType(ClassType.ELEMENTARY).grade(1)
                .memberType(MemberType.STUDENT).gender(GenderType.MALE)
                .address(new Address("반림동", "현대 아파트", "102-1201"))
                .attendanceStatus(AttendanceStatus.Y)
                .build();

        Parent parent = Parent.builder()
                .name("철수엄마").phoneNumber("010-1234-4567")
                .memberType(MemberType.PARENT).gender(GenderType.FEMALE)
                .build();

        // 연관관계 메서드 사용
        parent.addStudent(student);
        return parent;
    }

    public static Teacher createTeacher() {
        return Teacher.builder()
                .name("줄리아")
                .userId("julia@time.com").password("1234")
                .phoneNumber("010-1212-3456")
                .memberType(MemberType.TEACHER)
                .position(Position.TEACHER)
                .email("julia@time.com")
                .gender(GenderType.FEMALE)
                .build();
    }

    public static Answer createOneAnswer() {
        Student student = createStudent();
        Teacher teacher = createTeacher();
        Records record = createRecord(teacher, student);

        Answer answer = new Answer(record, teacher, "첫 댓글입니다.", AnswerStatus.GENERAL);
        answer.addRecords(record);
        answer.addTeacher(teacher);

        return answer;
    }

    public static StudentSaveReqDto createStudentDto() {
        StudentSaveReqDto saveReqDto = StudentSaveReqDto.builder()
                .name("철수")
                .userId("cs@time.com")
                .password("1234")
                .phoneNumber("010-1111-2222")
                .schoolName("용호초등학교")
                .classType(ClassType.ELEMENTARY)
                .grade(1)
                .memberType(MemberType.STUDENT)
                .gender(GenderType.MALE)
                .address(new Address("반림동", "현대 아파트", "102-1201"))
                .attendanceStatus(AttendanceStatus.Y)
                .build();
        return saveReqDto;
    }

    public static StudentUpdateReqDto updateStudentDto() {
        StudentUpdateReqDto updateReqDto = StudentUpdateReqDto.builder()
                .id(1L)
                .name("수정 엔티티")
                .userId("update@time.com")
                .password("1234")
                .phoneNumber("010-1111-2222")
                .schoolName("용호초등학교")
                .classType(ClassType.ELEMENTARY)
                .grade(1)
                .memberType(MemberType.STUDENT)
                .gender(GenderType.MALE)
                .address(new Address("반림동", "현대 아파트", "102-1201"))
                .attendanceStatus(AttendanceStatus.Y)
                .build();
        return updateReqDto;
    }
}
