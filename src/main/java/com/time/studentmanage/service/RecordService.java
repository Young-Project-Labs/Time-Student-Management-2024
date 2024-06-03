package com.time.studentmanage.service;

import com.time.studentmanage.config.RedisUtil;
import com.time.studentmanage.domain.dto.record.RecordRespDto;
import com.time.studentmanage.domain.dto.record.RecordSaveReqDto;
import com.time.studentmanage.domain.dto.record.RecordSearchDto;
import com.time.studentmanage.domain.dto.record.RecordSearchReqCondition;
import com.time.studentmanage.domain.enums.RecordStatus;
import com.time.studentmanage.domain.member.Student;
import com.time.studentmanage.domain.member.Teacher;
import com.time.studentmanage.domain.record.Record;
import com.time.studentmanage.exception.DataNotFoundException;
import com.time.studentmanage.exception.DateConvertException;
import com.time.studentmanage.repository.record.RecordRepository;
import com.time.studentmanage.repository.student.StudentRepository;
import com.time.studentmanage.repository.teacher.TeacherRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class RecordService {

    private final RedisUtil redisUtil;
    private final RecordRepository recordRepository;
    private final StudentRepository studentRepository;
    private final TeacherRepository teacherRepository;

    public Long saveRecord(RecordSaveReqDto recordSaveReqDTO) {

        Student studentPS = null;
        Teacher teacherPS = null;

        Optional<Student> studentOP = studentRepository.findById(recordSaveReqDTO.getStudentId());
        Optional<Teacher> teacherOP = teacherRepository.findById(recordSaveReqDTO.getTeacherId());

        if (studentOP.isPresent() && teacherOP.isPresent()) {
            studentPS = studentOP.get();
            teacherPS = teacherOP.get();
        }

        if (studentPS == null) {
            throw new DataNotFoundException("존재하지 않는 학생 정보입니다.");
        }

        if (teacherPS == null) {
            throw new DataNotFoundException("존재하지 않는 선생님 정보입니다.");
        }

        Record record = recordSaveReqDTO.toEntity(teacherPS, studentPS, recordSaveReqDTO.getTitle(), recordSaveReqDTO.getContent());

        record.addStudent(studentPS);
        record.addTeacher(teacherPS);

        Record result = recordRepository.save(record);

        return result.getId();
    }

    public void modifyContent(Long recordId, String title, String content) {
        Optional<Record> recordOP = recordRepository.findById(recordId);

        if (!recordOP.isPresent()) {
            throw new DataNotFoundException("존재하지 않는 피드백 입니다.");
        }

        Record recordPS = recordOP.get();
        recordPS.changeTitle(title);
        recordPS.changeContent(content);
    }

    public void deleteRecord(Long id) {
        Optional<Record> recordOP = recordRepository.findById(id);

        if (!recordOP.isPresent()) {
            throw new DataNotFoundException("존재하지 않는 피드백 입니다.");
        }

        Record recordPS = recordOP.get();

        if (recordPS.getStatus() == RecordStatus.DELETED) {
            return;
        }

        recordPS.changeRecordStatus(RecordStatus.DELETED);
    }

    @Transactional
    public RecordRespDto getRecord(Long recordId, Teacher teacher) {
        return caching(recordId, "teacher_" + teacher.getId());
    }

    @Transactional
    public RecordRespDto getRecord(Long recordId, Student student) {
        return caching(recordId, "student_" + student.getId());
    }

    @Transactional
    public RecordRespDto caching(Long recordId, String redisUserKey) {
        Optional<Record> recordOP = recordRepository.findById(recordId);

        if (!recordOP.isPresent()) {
            throw new DataNotFoundException("존재하지 않는 피드백 정보 입니다.");
        }

        Record r = recordOP.get();

        String values = redisUtil.getData(redisUserKey); // 유저 이름으로 생성된 키 값들을 조회

        if (values == null) { // 값이 없다면 새로 key-value 형태로 매핑해주기
            redisUtil.setDataExpire(redisUserKey, recordId + "_", calculateTimeUntilMidnight());
            r.addViewCount(r.getView());

        } else {
            StringBuffer sb = new StringBuffer();

            String[] valueBits = values.split("_");

            List<String> viewedRecordList = Arrays.asList(valueBits);

            boolean isView = false;

            if (!viewedRecordList.isEmpty()) {
                for (String id : viewedRecordList) {
                    if (String.valueOf(recordId).equals(id)) {
                        isView = true;
                        break;
                    }
                }
                if (!isView) {
                    sb.append(values).append(recordId).append("_");
                    redisUtil.setDataExpire(redisUserKey, sb.toString(), calculateTimeUntilMidnight());
                    r.addViewCount(r.getView());
                }
            }
        }

        RecordRespDto recordRespDTO = createRecordRespDto(r);

        return recordRespDTO;
    }

    private static RecordRespDto createRecordRespDto(Record r) {
        RecordRespDto recordRespDTO = new RecordRespDto();
        recordRespDTO.setRecordId(r.getId());
        recordRespDTO.setStudentName(r.getStudent().getName());
        recordRespDTO.setTitle(r.getTitle());
        recordRespDTO.setContent(r.getContent());
        recordRespDTO.setView(r.getView());
        recordRespDTO.setTeacherName(r.getTeacher().getName());
        recordRespDTO.setCreateDate(r.getCreateDate());
        recordRespDTO.setLastModifiedDate(r.getModifiedDate());
        return recordRespDTO;
    }

    public static long calculateTimeUntilMidnight() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime midnight = now.truncatedTo(ChronoUnit.DAYS).plusDays(1);
        return ChronoUnit.SECONDS.between(now, midnight);
    }

    /**
     * 기존 페이징 처리가 되지 않고 학생의 모든 피드백을 조회하던 메서드를
     * queryDSL, PageImpl을 사용하여 페이징 처리
     */
    @Transactional(readOnly = true)
    public Page<RecordRespDto> getAllStudentRecord(Long studentId, int page) {
        Optional<Student> studentOP = studentRepository.findById(studentId);

        if (!studentOP.isPresent()) {
            throw new DataNotFoundException("존재하지 않는 학생 정보입니다.");
        }

        Student studentPS = studentOP.get();
        Pageable pageable = PageRequest.of(page, 10);

        Page<RecordRespDto> pageList = recordRepository.findAllPaging(studentPS, pageable);

        return pageList;
    }

    @Transactional(readOnly = true)
    public Page<RecordRespDto> getPaginationResultWithSearchCondition(RecordSearchDto recordSearchDTO, Long studentId) {
        LocalDateTime[] convDate = new LocalDateTime[2];

        if (recordSearchDTO.getSearchType() == null) {
            throw new IllegalArgumentException("검색 조건을 선택하지 않았습니다.");
        }

        if (recordSearchDTO.getDates() == null || recordSearchDTO.getDates().equals("")) {
            throw new DateConvertException("날짜 조건이 선택되지 않았습니다.");
        }

        Student studentPS = validateStudentInfo(studentId);

        try {
            // "-"로 문자열을 나누고 양 옆 공백을 제거한 후 문자 배열로 변환
            String[] dates = Arrays.stream(recordSearchDTO.getDates().split("-"))
                    .map(String::trim)
                    .toArray(String[]::new);

            if (dates[0].equals(dates[1])) {
                convDate[0] = convDate[1] = null;
            } else {
                convDate[0] = convertLocalDateTime(dates[0]);
                convDate[1] = convertLocalDateTime(dates[1]);
            }

        } catch (IndexOutOfBoundsException e) {
            throw new DateConvertException("날짜 형식이 올바르게 입력되지 않았습니다.");
        }

        Pageable pageable = PageRequest.of(recordSearchDTO.getPage(), 10);

        RecordSearchReqCondition searchCondition = new RecordSearchReqCondition(studentPS, recordSearchDTO.getSearchType(), recordSearchDTO.getContent(), convDate[0], convDate[1], pageable);

        Page<RecordRespDto> pagingResult = recordRepository.findAllBySearchEngine(searchCondition);

        return pagingResult;
    }

    private Student validateStudentInfo(Long targetId) {
        Optional<Student> studentOP = studentRepository.findById(targetId);
        if (!studentOP.isPresent()) {
            throw new DataNotFoundException("학생 정보 없음");
        }

        return studentOP.get();
    }

    private LocalDateTime convertLocalDateTime(String date) {
        // "/"로 문자열을 나누고 숫자로 변환한 후 LocalDateTime으로 변환
        try {
            LocalDateTime convertedDate = Arrays.stream(date.split("/"))
                    .map(Integer::parseInt)
                    .collect(Collectors.collectingAndThen(
                            Collectors.toList(),
                            list -> LocalDateTime.of(list.get(0), list.get(1), list.get(2), 0, 0)
                    ));

            return convertedDate;
        } catch (IllegalArgumentException e) {
            throw new DateConvertException("날짜 형식이 잘 못 입력되었습니다.");
        } catch (IndexOutOfBoundsException e) {
            throw new DateConvertException("날짜 형식이 잘 못 입력되었습니다.");
        }
    }
}
