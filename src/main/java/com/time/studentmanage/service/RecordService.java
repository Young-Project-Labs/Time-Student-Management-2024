package com.time.studentmanage.service;

import com.time.studentmanage.domain.record.Record;
import com.time.studentmanage.domain.dto.record.RecordRespDto;
import com.time.studentmanage.domain.dto.record.RecordSaveReqDto;
import com.time.studentmanage.domain.dto.record.RecordSearchDto;
import com.time.studentmanage.domain.enums.RecordStatus;
import com.time.studentmanage.domain.member.Student;
import com.time.studentmanage.domain.member.Teacher;
import com.time.studentmanage.exception.DataNotFoundException;
import com.time.studentmanage.repository.student.StudentRepository;
import com.time.studentmanage.repository.teacher.TeacherRepository;
import com.time.studentmanage.repository.record.RecordRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class RecordService {

    private final RecordRepository recordRepository;
    private final StudentRepository studentRepository;
    private final TeacherRepository teacherRepository;

    private RecordRespDto createRecordRespDTO(Record r) {
        RecordRespDto recordRespDTO = new RecordRespDto();
        recordRespDTO.setRecordId(r.getId());
        recordRespDTO.setContent(r.getContent());
        recordRespDTO.setTeacherName(r.getTeacher().getName());
        recordRespDTO.setCreateDate(r.getCreateDate());
        recordRespDTO.setLastModifiedDate(r.getModifiedDate());
        recordRespDTO.setStatus(r.getStatus());
        recordRespDTO.setStudentName(r.getStudent().getName());
        return recordRespDTO;
    }

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

        Record record = recordSaveReqDTO.toEntity(teacherPS, studentPS, recordSaveReqDTO.getContent());

        record.addStudent(studentPS);
        record.addTeacher(teacherPS);

        Record result = recordRepository.save(record);

        return result.getId();
    }

    public void modifyContent(Long recordId, String content) {
        Optional<Record> recordOP = recordRepository.findById(recordId);

        if (!recordOP.isPresent()) {
            throw new DataNotFoundException("존재하지 않는 피드백 입니다.");
        }

        Record recordPS = recordOP.get();
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

    @Transactional(readOnly = true)
    public RecordRespDto getRecord(Long recordId) {
        Optional<Record> recordOP = recordRepository.findById(recordId);

        if (!recordOP.isPresent()) {
            throw new DataNotFoundException("존재하지 않는 피드백 정보 입니다.");
        }

        Record r = recordOP.get();
        RecordRespDto recordRespDTO = new RecordRespDto();
        recordRespDTO.setRecordId(r.getId());
        recordRespDTO.setStudentName(r.getStudent().getName());
        recordRespDTO.setContent(r.getContent());
        return recordRespDTO;
    }


    @Transactional(readOnly = true)
    public List<RecordRespDto> getStudentList(Long studentId) {
        Optional<Student> studentOP = studentRepository.findById(studentId);

        if (!studentOP.isPresent()) {
            throw new DataNotFoundException("존재하지 않는 학생 정보입니다.");
        }

        Student studentPS = studentOP.get();

        List<Record> recordList = recordRepository.findAllByStatusAndStudent(RecordStatus.PUBLISHED, studentPS);

        List<RecordRespDto> respList = new ArrayList<>();
        for (Record r : recordList) {
            RecordRespDto recordRespDTO = createRecordRespDTO(r);
            respList.add(recordRespDTO);
        }

        return respList;
    }

    @Transactional(readOnly = true)
    public List<RecordRespDto> getAllWrittenList(Long teacherId) {
        Optional<Teacher> teacherOP = teacherRepository.findById(teacherId);
        if (!teacherOP.isPresent()) {
            throw new DataNotFoundException("존재하지 않는 선생님 정보입니다.");
        }

        Teacher teacher = teacherOP.get();

        List<Record> recordList = recordRepository.findAllByTeacher(teacher);

        List<RecordRespDto> respList = new ArrayList<>();
        for (Record r : recordList) {
            RecordRespDto recordRespDTO = createRecordRespDTO(r);
            respList.add(recordRespDTO);
        }

        return respList;
    }

    @Transactional(readOnly = true)
    public List<RecordRespDto> getFilteredResults(RecordSearchDto recordSearchDTO) {
        List<RecordRespDto> respList = new ArrayList<>();
        LocalDateTime fromDate;
        LocalDateTime toDate;

        if (recordSearchDTO.getSearchType() == null) {
            throw new IllegalArgumentException("검색 조건을 선택하지 않았습니다.");
        }

        Student studentPS = validateStudentInfo(recordSearchDTO.getStudentId());

        // "-"로 문자열을 나누고 양 옆 공백을 제거한 후 문자 배열로 변환
        String[] dates = Arrays.stream(recordSearchDTO.getDates().split("-"))
                .map(String::trim)
                .toArray(String[]::new);

        if (dates[0].equals(dates[1])) {
            fromDate = toDate = null;
        } else {
            fromDate = convertLocalDateTime(dates[0]);
            toDate = convertLocalDateTime(dates[1]);
        }

        switch (recordSearchDTO.getSearchType()) {

            case CONTENT -> {
                List<Record> resultOfContentSearch = recordRepository.findAllByContentSearch(studentPS, recordSearchDTO.getContent(), fromDate, toDate);

                respList = resultOfContentSearch.stream()
                        .map(this::createRecordRespDTO)
                        .collect(Collectors.toList());
            }
            case TEACHER_NAME -> {
                List<Record> resultOfTeacherNameSearch = recordRepository.findAllByTeacherNameSearch(studentPS, recordSearchDTO.getContent(), fromDate, toDate);

                respList = resultOfTeacherNameSearch.stream()
                        .map(this::createRecordRespDTO)
                        .collect(Collectors.toList());
            }
        }

        return respList;
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
        return Arrays.stream(date.split("/"))
                .map(Integer::parseInt)
                .collect(Collectors.collectingAndThen(
                        Collectors.toList(),
                        list -> LocalDateTime.of(list.get(0), list.get(1), list.get(2), 0, 0)
                ));
    }
}
