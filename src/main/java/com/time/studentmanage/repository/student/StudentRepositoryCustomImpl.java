package com.time.studentmanage.repository.student;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.time.studentmanage.domain.dto.student.QStudentSearchRespDto;
import com.time.studentmanage.domain.dto.student.StudentSearchReqDto;
import com.time.studentmanage.domain.dto.student.StudentSearchRespDto;
import com.time.studentmanage.domain.enums.AttendanceStatus;
import com.time.studentmanage.domain.enums.SearchType;
import com.time.studentmanage.domain.member.QStudent;
import com.time.studentmanage.domain.member.Student;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;

@Repository
@Transactional
public class StudentRepositoryCustomImpl implements StudentRepositoryCustom {
    private final JPAQueryFactory query;

    public StudentRepositoryCustomImpl(EntityManager em) {
        this.query = new JPAQueryFactory(em);
    }

    public List<String> findAllSchoolName() {

        QStudent student = QStudent.student;

        List<String> result = query.selectDistinct(
                        student.schoolName)
                .from(student)
                .orderBy(student.schoolName.asc())
                .fetch();

        return result;
    }

    public Page<StudentSearchRespDto> findAllBySearchEngine(StudentSearchReqDto studentSearchReqDto, Pageable pageable) {

        QStudent student = QStudent.student;

        List<StudentSearchRespDto> fetch = query.select(
                        new QStudentSearchRespDto(
                                student.id,
                                student.name,
                                student.phoneNumber,
                                student.schoolName,
                                student.grade,
                                student.parentName,
                                student.parentPhoneNumber,
                                student.attendanceStatus,
                                student.createDate,
                                student.quitDate
                        )
                )
                .from(student)
                .where(student.attendanceStatus.eq(AttendanceStatus.Y),
                        likeSearchTypeAndContent(studentSearchReqDto.getSearchType(), studentSearchReqDto.getContent())
                )
                .orderBy(student.createDate.desc(), student.name.asc(), student.grade.asc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Long> count = query.select(student.count())
                .from(student)
                .where(student.attendanceStatus.eq(AttendanceStatus.Y),
                        likeSearchTypeAndContent(studentSearchReqDto.getSearchType(), studentSearchReqDto.getContent())
                );


        return PageableExecutionUtils.getPage(fetch, pageable, count::fetchOne);
    }

    private BooleanExpression likeSearchTypeAndContent(SearchType searchType, String content) {
        if (!StringUtils.hasText(content) || searchType == null) {
            return null;
        }

        switch (searchType) {
            case STUDENT_NAME:
                break;
            case SCHOOL_NAME:
                return QStudent.student.schoolName.like("%" + content + "%");
            case PARENT_NAME:
                return QStudent.student.parentName.like("%" + content + "%");
        }

        return QStudent.student.name.like("%" + content + "%");
    }

    public List<Student> findAllBySearchEngineWithNameNotIncludeClass(String studentName) {

        QStudent student = QStudent.student;

        List<Student> result = query.selectFrom(student)
                .where(student.attendanceStatus.eq(AttendanceStatus.Y),
                        likeStudentName(studentName)
                                .and(student.classRoom.isNull())
                )
                .fetch();

        return result;
    }

    private BooleanExpression likeStudentName(String studentName) {
        if (!StringUtils.hasText(studentName)) {
            return null;
        }

        return QStudent.student.name.like("%" + studentName + "%");
    }
}
