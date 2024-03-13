package com.time.studentmanage.service;

import com.time.studentmanage.domain.Records;
import com.time.studentmanage.domain.dto.RecordSaveReqDTO;
import com.time.studentmanage.domain.member.Student;
import com.time.studentmanage.domain.member.Teacher;
import com.time.studentmanage.repository.RecordsRepository;
import com.time.studentmanage.repository.StudentRepository;
import com.time.studentmanage.repository.TeacherRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class RecordService {

    private final RecordsRepository recordsRepository;
    private final StudentRepository studentRepository;
    private final TeacherRepository teacherRepository;

    public Long saveFeedback(RecordSaveReqDTO recordSaveReqDTO) {

        Student studentPS = null;
        Teacher teacherPS = null;

        Optional<Student> studentOP = studentRepository.findById(recordSaveReqDTO.getStudentId());
        Optional<Teacher> teacherOP = teacherRepository.findById(recordSaveReqDTO.getTeacherId());

        if (studentOP.isPresent() && teacherOP.isPresent()) {
            studentPS = studentOP.get();
            teacherPS = teacherOP.get();
        }

        if (studentPS == null) {
            // 학생이 없어요
        }

        if (teacherPS == null) {
            // 선생이 없어요
        }

        Records record = recordSaveReqDTO.toEntity(teacherPS, studentPS, recordSaveReqDTO.getContent());
        Records result = recordsRepository.save(record);

        return result.getId();
    }

    public void modifyContent(Long recordId, String content) {
        Optional<Records> recordOP = recordsRepository.findById(recordId);

        if (recordOP.isPresent()) {
            Records recordPS = recordOP.get();
            recordPS.changeContent(content);
        } else {
            // TODO: 예외 발생 시키기
        }
    }
}
