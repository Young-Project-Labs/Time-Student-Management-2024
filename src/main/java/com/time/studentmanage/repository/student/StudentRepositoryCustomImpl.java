package com.time.studentmanage.repository.student;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.time.studentmanage.domain.dto.student.*;
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

    public List<SchoolRespDto> findAllSchoolName() {

        QStudent student = QStudent.student;

        List<SchoolRespDto> fetch = query.selectDistinct(
                        new QSchoolRespDto(
                                student.classType,
                                student.schoolName
                        ))
                .from(student)
                .orderBy(student.schoolName.asc())
                .fetch();

        return fetch;
    }

    /**
     * 홈페이지에서 학생 정보를 검색할 때 동작하는 검색 메서드
     */
    public Page<SelectedSchoolRespDto> findAllBySelectedSchoolName(String schoolName, String studentName, Pageable pageable) {
        QStudent student = QStudent.student;

        List<SelectedSchoolRespDto> fetch = query.select(
                        new QSelectedSchoolRespDto(
                                student.id,
                                student.name,
                                student.grade
                        )
                ).from(student)
                .where(student.attendanceStatus.eq(AttendanceStatus.Y)
                        .and(student.schoolName.eq(schoolName)),
                        likeStudentName(studentName)
                )
                .orderBy(student.grade.asc(), student.name.asc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Long> count = query.select(student.count())
                .from(student)
                .where(student.attendanceStatus.eq(AttendanceStatus.Y)
                        .and(student.schoolName.eq(schoolName)),
                        likeStudentName(studentName)
                );

        return PageableExecutionUtils.getPage(fetch, pageable, count::fetchOne);
    }

    /**
     * 학생 목록에서 동작하는 검색 메서드
     */
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
                .where(likeSearchTypeAndContent(studentSearchReqDto.getSearchType(), studentSearchReqDto.getContent())
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
        if (StringUtils.hasText(content) || searchType != null) {
            switch (searchType) {
                case STUDENT_NAME:
                    return QStudent.student.name.like("%" + content + "%");
                case SCHOOL_NAME:
                    return QStudent.student.schoolName.like("%" + content + "%");
                case PARENT_NAME:
                    return QStudent.student.parentName.like("%" + content + "%");
            }
        }

        return null;
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
