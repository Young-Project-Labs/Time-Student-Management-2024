package com.time.studentmanage;

import com.time.studentmanage.domain.Address;
import com.time.studentmanage.domain.Record;
import com.time.studentmanage.domain.dto.teacher.TeacherUpdateReqDto;
import com.time.studentmanage.domain.enums.*;
import com.time.studentmanage.domain.member.Student;
import com.time.studentmanage.domain.member.Teacher;
import com.time.studentmanage.domain.dto.student.StudentSaveReqDto;
import com.time.studentmanage.domain.dto.student.StudentUpdateReqDto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class TestUtil {
    public static Record createRecord(Teacher teacher, Student student) {
        return Record.builder()
                .teacher(teacher)
                .student(student)
                .content("문법 수준이 높습니다. 테스트 후 초등 고학년 문법반으로 올려도 될 것 같습니다.")
                .status(RecordStatus.PUBLISHED)
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

    public static StudentSaveReqDto createStudentDto() {
        StudentSaveReqDto saveReqDto = new StudentSaveReqDto("철수","cs@time.com","1234","010-1111-2222","철수어머님","010-9999-9999","용호초등학교",1,AttendanceStatus.Y, MemberType.STUDENT,GenderType.MALE,ClassType.ELEMENTARY,new Address("반림동", "현대 아파트", "102-1201"));

        return saveReqDto;
    }

    public static StudentUpdateReqDto updateStudentDto() {
        StudentUpdateReqDto updateReqDto = new StudentUpdateReqDto(1L, "수정 엔티티","010-1111-3333", "용호초등학교", 1, AttendanceStatus.Y,LocalDateTime.now(), MemberType.STUDENT,GenderType.MALE,ClassType.ELEMENTARY,new Address("반림동", "현대 아파트", "102-1201"));
        return updateReqDto;
    }

    public static TeacherUpdateReqDto updateTeacherDto() {
        return TeacherUpdateReqDto.builder()
                .name("이름수정")
                .phoneNumber("010-9999-9999")
                .position(Position.TEACHER)
                .memberType(MemberType.TEACHER)
                .email("julia@time.com")
                .gender(GenderType.FEMALE)
                .build();
    }
}
