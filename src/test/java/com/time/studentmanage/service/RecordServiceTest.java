package com.time.studentmanage.service;

import com.time.studentmanage.domain.dto.record.RecordSaveReqDto;
import com.time.studentmanage.domain.enums.RecordStatus;
import com.time.studentmanage.domain.member.Student;
import com.time.studentmanage.domain.member.Teacher;
import com.time.studentmanage.domain.record.Record;
import com.time.studentmanage.exception.DataNotFoundException;
import com.time.studentmanage.repository.record.RecordRepository;
import com.time.studentmanage.repository.student.StudentRepository;
import com.time.studentmanage.repository.teacher.TeacherRepository;
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
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@Slf4j
@ExtendWith(MockitoExtension.class)
class RecordServiceTest {

    @Mock
    RecordRepository recordRepository;
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
        String title = "테스트 제목 입니다.";
        String content = "피드백 내용입니다.";

        Student student = createStudent();
        Teacher teacher = createTeacher();
        ReflectionTestUtils.setField(student, "id", fakeId);
        ReflectionTestUtils.setField(teacher, "id", fakeId);

        // stub 1
        when(studentRepository.findById(anyLong())).thenReturn(Optional.of(student));
        when(teacherRepository.findById(anyLong())).thenReturn(Optional.of(teacher));

        // stub 2
        Record record = Record.builder()
                .teacher(teacher)
                .student(student)
                .title(title)
                .content(content)
                .build();
        ReflectionTestUtils.setField(record, "id", fakeId);

        when(recordRepository.save(any())).thenReturn(record);

        // 1. 저장 요청
        RecordSaveReqDto recordSaveReqDTO = new RecordSaveReqDto(fakeId, fakeId, title, content);

        //when
        Long recordId = recordService.saveRecord(recordSaveReqDTO);

        //then
        assertThat(recordId).isEqualTo(fakeId);
        assertThat(record.getStudent().getName()).isEqualTo("철수");
        assertThat(record.getContent()).isEqualTo(content);
    }

    @Test
    void 학생_정보가_없을_때_피드백_저장_실패_테스트() {
        //given
        Long fakeId = 1L;
        String title = "피드백 제목입니다.";
        String content = "피드백 내용입니다.";

        Teacher teacher = createTeacher();
        ReflectionTestUtils.setField(teacher, "id", fakeId);

        // stub 1
        when(studentRepository.findById(anyLong())).thenReturn(Optional.empty());
        when(teacherRepository.findById(anyLong())).thenReturn(Optional.of(teacher));

        // stub 2
        Record record = Record.builder()
                .teacher(teacher)
                .student(null)
                .title(title)
                .content(content)
                .build();
        ReflectionTestUtils.setField(record, "id", fakeId);

//        when(recordsRepository.save(any())).thenReturn(record); // 테스트에서 사용되지 않는 스텁을 넣으면 테스트 오류 발생함

        // 1. 저장 요청
        RecordSaveReqDto recordSaveReqDTO = new RecordSaveReqDto(fakeId, fakeId, title, content);


        //when
        //then
        assertThatThrownBy(() -> recordService.saveRecord(recordSaveReqDTO))
                .isInstanceOf(DataNotFoundException.class);

    }

    @Test
    void 선생님_정보가_없을_때_피드백_저장_실패_테스트() {
        //given
        Long fakeId = 1L;
        String title = "피드백 제목입니다.";
        String content = "피드백 내용입니다.";

        Student student = createStudent();
        ReflectionTestUtils.setField(student, "id", fakeId);

        // stub 1
        when(studentRepository.findById(anyLong())).thenReturn(Optional.of(student));
        when(teacherRepository.findById(anyLong())).thenReturn(Optional.empty());

        // stub 2
        Record record = Record.builder()
                .teacher(null)
                .student(student)
                .title(title)
                .content(content)
                .build();
        ReflectionTestUtils.setField(record, "id", fakeId);

        // 1. 저장 요청
        RecordSaveReqDto recordSaveReqDTO = new RecordSaveReqDto(fakeId, fakeId, title, content);

        //when
        //then
        assertThatThrownBy(() -> recordService.saveRecord(recordSaveReqDTO))
                .isInstanceOf(DataNotFoundException.class);

    }

    @Test
    void 피드백_수정_서비스_로직() {
        //given
        Long fakeId = 1L;
        String title = "수정된 제목 입니다.";
        String content = "수정된 피드백 입니다.";

        Student student = createStudent();
        Teacher teacher = createTeacher();
        ReflectionTestUtils.setField(student, "id", fakeId);
        ReflectionTestUtils.setField(teacher, "id", fakeId);

        // stub
        Record record = createRecord(teacher, student);
        log.info("before save record={}", record.getContent());
        ReflectionTestUtils.setField(record, "id", fakeId);

        when(recordRepository.findById(anyLong())).thenReturn(Optional.of(record));

        recordService.modifyContent(record.getId(), title, content);
        log.info("after save record={}", record.getContent());

        assertThat(record.getContent()).isEqualTo(content);
        assertThat(record.getTitle()).isEqualTo(title);

    }

    @Test
    void 피드백_수정_실패_로직() {
        //given
        Long fakeId = 1L;
        String title = "수정된 제목 입니다.";
        String content = "수정된 피드백 입니다.";

        // stub
        Record record = Record.builder()
                .content(content)
                .build();
        ReflectionTestUtils.setField(record, "id", fakeId);

        when(recordRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> recordService.modifyContent(record.getId(), title, content))
                .isInstanceOf(DataNotFoundException.class);
    }

    @Test
    void 피드백_삭제_테스트() {
        //given
        Long fakeId = 1L;
        String content = "수정된 피드백 입니다.";

        // stub
        Record record = Record.builder()
                .content(content)
                .status(RecordStatus.PUBLISHED)
                .build();

        ReflectionTestUtils.setField(record, "id", fakeId);

        when(recordRepository.findById(anyLong())).thenReturn(Optional.of(record));

        //when
        recordService.deleteRecord(record.getId());

        //then
        assertThat(record.getStatus()).isEqualTo(RecordStatus.DELETED);
    }

}