package com.time.studentmanage.repository.teacher;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.time.studentmanage.domain.dto.teacher.QTeacherRespDto;
import com.time.studentmanage.domain.dto.teacher.TeacherRespDto;
import com.time.studentmanage.domain.dto.teacher.TeacherSearchReqDto;
import com.time.studentmanage.domain.enums.SearchType;
import com.time.studentmanage.domain.member.QTeacher;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public class TeacherRepositoryCustomImpl implements TeacherRepositoryCustom {
    private final JPAQueryFactory query;

    public TeacherRepositoryCustomImpl(EntityManager em) {
        this.query = new JPAQueryFactory(em);
    }


    public Page<TeacherRespDto> findAllPaging(Pageable pageable) {

        QTeacher teacher = QTeacher.teacher;

        List<TeacherRespDto> fetch = query.select(
                        new QTeacherRespDto(
                                teacher)
                )
                .from(teacher)
                .orderBy(teacher.position.asc()) // TODO: 직급별 정렬 수정 필요.
                .orderBy(teacher.id.asc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Long> count = query.select(teacher.count())
                .from(teacher);

        return PageableExecutionUtils.getPage(fetch, pageable, count::fetchOne);
    }

    public Page<TeacherRespDto> findSearchDtoPaging(TeacherSearchReqDto searchReqDto, Pageable pageable) {

        QTeacher teacher = QTeacher.teacher;

        List<TeacherRespDto> fetch = query.select(
                        new QTeacherRespDto(
                                teacher)
                )
                .from(teacher)
                .where(searchCondition(searchReqDto))
                .orderBy(teacher.position.asc()) // TODO: 직급별 정렬 수정 필요.
                .orderBy(teacher.id.asc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Long> count = query.select(teacher.count())
                .from(teacher);

        return PageableExecutionUtils.getPage(fetch, pageable, count::fetchOne);
    }

    // SearchType에 따른 조건 생성
    private BooleanExpression searchCondition(TeacherSearchReqDto searchReqDto) {
        QTeacher teacher = QTeacher.teacher;
        if (searchReqDto.getSearchType() != null && searchReqDto.getContent() != null) {
            if (searchReqDto.getSearchType().equals(SearchType.EMAIL)) {
                // 이메일로 검색
                return teacher.email.like("%" + searchReqDto.getContent() + "%");
            } else if (searchReqDto.getSearchType().equals(SearchType.TEACHER_NAME)) {
                // 선생님 이름으로 검색
                return teacher.name.like("%" + searchReqDto.getContent() + "%");
            }
        }
        return null; // 조건이 없으면 null을 반환
    }


}
