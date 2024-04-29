package com.time.studentmanage.repository.record;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.time.studentmanage.domain.dto.record.QRecordRespDto;
import com.time.studentmanage.domain.dto.record.RecordRespDto;
import com.time.studentmanage.domain.dto.record.RecordSearchDto;
import com.time.studentmanage.domain.enums.RecordStatus;
import com.time.studentmanage.domain.enums.SearchType;
import com.time.studentmanage.domain.member.Student;
import com.time.studentmanage.domain.record.QRecord;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StreamUtils;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Repository
@Transactional
public class RecordRepositoryCustomImpl implements RecordRepositoryCustom {

    private final JPAQueryFactory query;

    public RecordRepositoryCustomImpl(EntityManager em) {
        this.query = new JPAQueryFactory(em);
    }

    public Page<RecordRespDto> findAllBySearchEngine(Student student,
                                                     SearchType searchType, String content,
                                                     LocalDateTime fromDate, LocalDateTime toDate,
                                                     Pageable pageable) {
        QRecord record = QRecord.record;

        List<RecordRespDto> fetch = query.select(
                        new QRecordRespDto(
                                record.id,
                                record.content,
                                record.teacher.name,
                                record.student.name,
                                record.status,
                                record.createDate,
                                record.modifiedDate)
                )
                .from(record)
                .where(record.status.eq(RecordStatus.PUBLISHED)
                                .and(record.student.eq(student)),
                        likeSearchTypeAndContent(searchType, content)
                )
                .orderBy(record.createDate.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Long> count = query.select(record.count())
                .from(record)
                .where(record.status.eq(RecordStatus.PUBLISHED)
                                .and(record.student.eq(student)),
                        likeSearchTypeAndContent(searchType, content),
                        betweenDateRange(fromDate, toDate)
                );

        return PageableExecutionUtils.getPage(fetch, pageable, count::fetchOne);
    }

    public Page<RecordRespDto> findAllPaging(Student student, Pageable pageable) {
        QRecord record = QRecord.record;

        List<RecordRespDto> fetch = query.select(
                        new QRecordRespDto(record.id,
                                record.content,
                                record.teacher.name,
                                record.student.name,
                                record.status,
                                record.createDate,
                                record.modifiedDate)
                )
                .from(record)
                .where(record.status.eq(RecordStatus.PUBLISHED)
                        .and(record.student.eq(student)))
                .orderBy(record.createDate.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Long> count = query.select(record.count())
                .from(record)
                .where(record.status.eq(RecordStatus.PUBLISHED)
                        .and(record.student.eq(student)));

        return PageableExecutionUtils.getPage(fetch, pageable, count::fetchOne);
    }

    private BooleanExpression betweenDateRange(LocalDateTime fromDate, LocalDateTime toDate) {

        String today = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE);

        if (fromDate == null && toDate == null) {
            return null;
        }
        if (fromDate.format(DateTimeFormatter.ISO_LOCAL_DATE).equals(today) && toDate.format(DateTimeFormatter.ISO_LOCAL_DATE).equals(today)) {
            return null;
        }

        LocalDateTime toDatePlusOneDay = toDate.plusDays(1);
        return QRecord.record.createDate.goe(fromDate).and(QRecord.record.createDate.loe(toDatePlusOneDay));
    }

    private BooleanExpression likeSearchTypeAndContent(SearchType searchType, String content) {
        if (!StringUtils.hasText(content)) {
            return null;
        }

        if (searchType == SearchType.TEACHER_NAME) {
            return QRecord.record.teacher.name.like("%" + content + "%");
        }


        return QRecord.record.content.like("%" + content + "%");
    }

    private BooleanExpression likeSearchWords(String searchWords) {

        if (!StringUtils.hasText(searchWords)) {
            return null;
        }
        return QRecord.record.content.like("%" + searchWords + "%");
    }

    private BooleanExpression likeTeacherName(String teacherName) {

        if (!StringUtils.hasText(teacherName)) {
            return null;
        }
        return QRecord.record.teacher.name.like("%" + teacherName + "%");
    }
}
