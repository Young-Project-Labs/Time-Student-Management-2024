package com.time.studentmanage.repository.record;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.time.studentmanage.domain.enums.RecordStatus;
import com.time.studentmanage.domain.member.Student;
import com.time.studentmanage.domain.record.QRecord;
import com.time.studentmanage.domain.record.Record;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
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

    // 내용을 선택 + 검색어를 입력 + 날짜 기간을 선택
    public List<Record> findAllByContentSearch(Student student, String searchWords,
                                               LocalDateTime fromDate, LocalDateTime toDate) {
        QRecord record = QRecord.record;

        List<Record> result = query.selectFrom(record)
                .where(record.status.eq(RecordStatus.PUBLISHED)
                                .and(record.student.eq(student)),
                        likeSearchWords(searchWords),
                        betweenDateRange(fromDate, toDate))
                .orderBy(record.createDate.desc())
                .fetch();

        return result;
    }

    public List<Record> findAllByTeacherNameSearch(Student student, String teacherName,
                                                   LocalDateTime fromDate, LocalDateTime toDate) {
        QRecord record = QRecord.record;

        List<Record> result = query.selectFrom(record)
                .where(record.status.eq(RecordStatus.PUBLISHED)
                                .and(record.student.eq(student)),
                        likeTeacherName(teacherName),
                        betweenDateRange(fromDate, toDate))
                .orderBy(record.createDate.desc())
                .fetch();

        return result;
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
