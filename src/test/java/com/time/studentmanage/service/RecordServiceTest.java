package com.time.studentmanage.service;

import com.time.studentmanage.domain.Records;
import com.time.studentmanage.domain.dto.RecordSaveReqDTO;
import com.time.studentmanage.domain.member.Student;
import com.time.studentmanage.domain.member.Teacher;
import com.time.studentmanage.repository.RecordsRepository;
import com.time.studentmanage.repository.StudentRepository;
import com.time.studentmanage.repository.TeacherRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;

import static com.time.studentmanage.TestUtil.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@Slf4j
@ExtendWith(MockitoExtension.class)
class RecordServiceTest {

    @Mock
    RecordsRepository recordsRepository;
    @Mock
    StudentRepository studentRepository;
    @Mock
    TeacherRepository teacherRepository;

    @InjectMocks
    RecordService recordService;

    @Test
    void 피드백_저장_서비스_로직() {
        //given
        Long fakeId = 1L;
        String content = "피드백 내용입니다.";

        Student student = createStudent();
        Teacher teacher = createTeacher();
        ReflectionTestUtils.setField(student, "id", fakeId);
        ReflectionTestUtils.setField(teacher, "id", fakeId);

        // stub 1
        when(studentRepository.findById(anyLong())).thenReturn(Optional.of(student));
        when(teacherRepository.findById(anyLong())).thenReturn(Optional.of(teacher));

        // stub 2
        Records records = Records.builder()
                .teacher(teacher)
                .student(student)
                .content(content)
                .build();
        ReflectionTestUtils.setField(records, "id", fakeId);

        when(recordsRepository.save(any())).thenReturn(records);

        // 1. 저장 요청
        RecordSaveReqDTO recordSaveReqDTO = RecordSaveReqDTO.builder()
                .studentId(fakeId)
                .teacherId(fakeId)
                .content(content)
                .build();

        //when
        Long recordId = recordService.saveFeedback(recordSaveReqDTO);

        //then
        assertThat(recordId).isEqualTo(fakeId);
        assertThat(records.getStudent().getName()).isEqualTo("철수");
        assertThat(records.getContent()).isEqualTo(content);

    }

    @Test
    void 피드백_수정_서비스_로직() {
        //given
        Long fakeId = 1L;
        String content = "수정된 피드백 입니다.";

        Student student = createStudent();
        Teacher teacher = createTeacher();
        ReflectionTestUtils.setField(student, "id", fakeId);
        ReflectionTestUtils.setField(teacher, "id", fakeId);

        // stub
        Records record = createRecord(teacher, student);
        log.info("before save record={}", record.getContent());
        ReflectionTestUtils.setField(record, "id", fakeId);

        when(recordsRepository.findById(anyLong())).thenReturn(Optional.of(record));

        recordService.modifyContent(record.getId(), content);
        log.info("after save record={}", record.getContent());

        assertThat(record.getContent()).isEqualTo(content);
    }

}