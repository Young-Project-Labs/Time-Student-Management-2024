package com.time.studentmanage.repository.classroom;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.time.studentmanage.domain.classroom.QClassRoom;
import com.time.studentmanage.domain.dto.classroom.ClassRoomRespDto;
import com.time.studentmanage.domain.dto.classroom.ClassRoomSearchReqDto;
import com.time.studentmanage.domain.dto.classroom.QClassRoomRespDto;
import com.time.studentmanage.domain.enums.SearchType;
import com.time.studentmanage.domain.member.Teacher;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.List;

@Repository
public class ClassRoomRepositoryCustomImpl implements ClassRoomRepositoryCustom {
    private final JPAQueryFactory query;

    public ClassRoomRepositoryCustomImpl(EntityManager em) {
        this.query = new JPAQueryFactory(em);
    }

    public Page<ClassRoomRespDto> findAllPaging(Teacher teacher, Pageable pageable) {
        QClassRoom classRoom = QClassRoom.classRoom;

        List<ClassRoomRespDto> fetch = query.select(
                        new QClassRoomRespDto(
                                classRoom.id,
                                classRoom.name,
                                classRoom.classInfo,
                                classRoom.classType,
                                classRoom.studentList.size()
                        )
                )
                .from(classRoom)
                .where(classRoom.teacher.eq(teacher))
                .orderBy(classRoom.name.asc(),
                        classRoom.createDate.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Long> count = query.select(classRoom.count())
                .from(classRoom);

        return PageableExecutionUtils.getPage(fetch, pageable, count::fetchOne);
    }

    public Page<ClassRoomRespDto> findAllBySearchEngine(ClassRoomSearchReqDto classRoomSearchReqDto, Pageable pageable) {
        QClassRoom classRoom = QClassRoom.classRoom;

        Long teacherId = classRoomSearchReqDto.getTeacherId();

        List<ClassRoomRespDto> fetch = query.select(
                        new QClassRoomRespDto(
                                classRoom.id,
                                classRoom.name,
                                classRoom.classInfo,
                                classRoom.classType,
                                classRoom.studentList.size()
                        )
                )
                .from(classRoom)
                .where(classRoom.teacher.id.eq(teacherId),
                        likeSearchTypeAndContent(classRoomSearchReqDto.getSearchType(), classRoomSearchReqDto.getContent()))
                .orderBy(classRoom.name.asc(),
                        classRoom.createDate.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Long> count = query.select(classRoom.count())
                .from(classRoom)
                .where(classRoom.teacher.id.eq(teacherId),
                        likeSearchTypeAndContent(classRoomSearchReqDto.getSearchType(), classRoomSearchReqDto.getContent()));

        return PageableExecutionUtils.getPage(fetch, pageable, count::fetchOne);
    }

    private BooleanExpression likeSearchTypeAndContent(SearchType searchType, String content) {
        if (!StringUtils.hasText(content)) {
            return null;
        }

        switch (searchType) {
            case CLASS_INFO -> {
                return QClassRoom.classRoom.classInfo.like("%" + content + "%");
            }
        }

        return QClassRoom.classRoom.name.like("%" + content + "%");
    }
}
