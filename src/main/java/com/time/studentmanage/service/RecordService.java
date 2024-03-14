package com.time.studentmanage.service;

import com.time.studentmanage.domain.Records;
import com.time.studentmanage.domain.dto.RecordSaveReqDTO;
import com.time.studentmanage.domain.enums.RecordStatus;
import com.time.studentmanage.domain.member.Student;
import com.time.studentmanage.domain.member.Teacher;
import com.time.studentmanage.exception.DataNotFoundException;
import com.time.studentmanage.repository.RecordsRepository;
import com.time.studentmanage.repository.StudentRepository;
import com.time.studentmanage.repository.TeacherRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class RecordService {

    private final RecordsRepository recordsRepository;
    private final StudentRepository studentRepository;
    private final TeacherRepository teacherRepository;

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

        Records record = recordSaveReqDTO.toEntity(teacherPS, studentPS, recordSaveReqDTO.getContent());
        Records result = recordsRepository.save(record);

        return result.getId();
    }

    public void modifyContent(Long recordId, String content) {
        Optional<Records> recordOP = recordsRepository.findById(recordId);

        if (!recordOP.isPresent()) {
            throw new DataNotFoundException("존재하지 않는 피드백 입니다.");
        }

        Records recordPS = recordOP.get();
        recordPS.changeContent(content);
    }

    public void deleteRecord(Long id) {
        Optional<Records> recordOP = recordsRepository.findById(id);

        if (!recordOP.isPresent()) {
            throw new DataNotFoundException("존재하지 않는 피드백 입니다.");
        }

        Records recordPS = recordOP.get();

        if (recordPS.getStatus() == RecordStatus.DELETED) {
            return;
        }

        recordPS.changeRecordStatus(RecordStatus.DELETED);
    }

    public List<Records> getStudentList(Long studentId) {
        Optional<Student> studentOP = studentRepository.findById(studentId);

        if (!studentOP.isPresent()) {
            throw new DataNotFoundException("존재하지 않는 학생 정보입니다.");
        }

        Student studentPS = studentOP.get();
        return recordsRepository.findAllByStatusAndStudent(RecordStatus.PUBLISHED, studentPS);
    }

    public List<Records> getAllWrittenList(Long teacherId) {
        Optional<Teacher> teacherOP = teacherRepository.findById(teacherId);
        if (!teacherOP.isPresent()) {
            throw new DataNotFoundException("존재하지 않는 선생님 정보입니다.");
        }

        Teacher teacher = teacherOP.get();

        return recordsRepository.findAllByTeacher(teacher);
    }
}
