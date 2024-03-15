package com.time.studentmanage.service;

import com.time.studentmanage.domain.Record;
import com.time.studentmanage.domain.dto.record.RecordRespDTO;
import com.time.studentmanage.domain.dto.record.RecordSaveReqDTO;
import com.time.studentmanage.domain.enums.RecordStatus;
import com.time.studentmanage.domain.member.Student;
import com.time.studentmanage.domain.member.Teacher;
import com.time.studentmanage.exception.DataNotFoundException;
import com.time.studentmanage.repository.RecordRepository;
import com.time.studentmanage.repository.StudentRepository;
import com.time.studentmanage.repository.TeacherRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class RecordService {

    private final RecordRepository recordRepository;
    private final StudentRepository studentRepository;
    private final TeacherRepository teacherRepository;

    private static RecordRespDTO createRecordRespDTO(Record r) {
        RecordRespDTO recordRespDTO = new RecordRespDTO();
        recordRespDTO.setId(r.getId());
        recordRespDTO.setContent(r.getContent());
        recordRespDTO.setTeacherName(r.getTeacher().getName());
        recordRespDTO.setAnswerList(r.getAnswerList());
        recordRespDTO.setLastModifiedDate(r.getModifiedDate());
        return recordRespDTO;
    }

    public Long saveRecord(RecordSaveReqDTO recordSaveReqDTO) {

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
    public List<RecordRespDTO> getStudentList(Long studentId) {
        Optional<Student> studentOP = studentRepository.findById(studentId);

        if (!studentOP.isPresent()) {
            throw new DataNotFoundException("존재하지 않는 학생 정보입니다.");
        }

        Student studentPS = studentOP.get();

        List<Record> recordList = recordRepository.findAllByStatusAndStudent(RecordStatus.PUBLISHED, studentPS);

        List<RecordRespDTO> respList = new ArrayList<>();
        for (Record r : recordList) {
            RecordRespDTO recordRespDTO = createRecordRespDTO(r);
            respList.add(recordRespDTO);
        }

        return respList;
    }

    @Transactional(readOnly = true)
    public List<RecordRespDTO> getAllWrittenList(Long teacherId) {
        Optional<Teacher> teacherOP = teacherRepository.findById(teacherId);
        if (!teacherOP.isPresent()) {
            throw new DataNotFoundException("존재하지 않는 선생님 정보입니다.");
        }

        Teacher teacher = teacherOP.get();

        List<Record> recordList = recordRepository.findAllByTeacher(teacher);

        List<RecordRespDTO> respList = new ArrayList<>();
        for (Record r : recordList) {
            RecordRespDTO recordRespDTO = createRecordRespDTO(r);
            respList.add(recordRespDTO);
        }

        return respList;
    }
}
