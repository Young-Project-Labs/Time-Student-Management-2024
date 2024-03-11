package com.time.studentmanage;

import com.time.studentmanage.domain.Address;
import com.time.studentmanage.domain.Answer;
import com.time.studentmanage.domain.Records;
import com.time.studentmanage.domain.enums.*;
import com.time.studentmanage.domain.member.Student;
import com.time.studentmanage.domain.member.Teacher;

public class TestUtil {
    public static Records createRecord(Teacher teacher, Student student) {
        return Records.builder()
                .teacher(teacher)
                .student(student)
                .content("문법 수준이 높습니다. 테스트 후 초등 고학년 문법반으로 올려도 될 것 같습니다.")
                .build();
    }

    public static Student createStudent() {
        Student student = new Student("철수", "cs@time.com", "1234", "010-1111-2222", "용호초등학교", ClassType.ELEMENTARY, 1, MemberType.STUDENT, GenderType.MALE, new Address("반림동", "현대 아파트", "102-1201"), AttendanceStatus.Y);
        return student;
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
}
