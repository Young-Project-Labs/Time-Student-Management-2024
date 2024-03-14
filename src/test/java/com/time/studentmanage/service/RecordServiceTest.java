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
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;
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
        Long recordId = recordService.saveRecord(recordSaveReqDTO);

        //then
        assertThat(recordId).isEqualTo(fakeId);
        assertThat(records.getStudent().getName()).isEqualTo("철수");
        assertThat(records.getContent()).isEqualTo(content);
    }

    @Test
    void 학생_정보가_없을_때_피드백_저장_실패_테스트() {
        //given
        Long fakeId = 1L;
        String content = "피드백 내용입니다.";

        Teacher teacher = createTeacher();
        ReflectionTestUtils.setField(teacher, "id", fakeId);

        // stub 1
        when(studentRepository.findById(anyLong())).thenReturn(Optional.empty());
        when(teacherRepository.findById(anyLong())).thenReturn(Optional.of(teacher));

        // stub 2
        Records records = Records.builder()
                .teacher(teacher)
                .student(null)
                .content(content)
                .build();
        ReflectionTestUtils.setField(records, "id", fakeId);

//        when(recordsRepository.save(any())).thenReturn(records); // 테스트에서 사용되지 않는 스텁을 넣으면 테스트 오류 발생함

        // 1. 저장 요청
        RecordSaveReqDTO recordSaveReqDTO = RecordSaveReqDTO.builder()
                .studentId(fakeId)
                .teacherId(fakeId)
                .content(content)
                .build();

        //when
        //then
        assertThatThrownBy(() -> recordService.saveRecord(recordSaveReqDTO))
                .isInstanceOf(DataNotFoundException.class);

    }

    @Test
    void 선생님_정보가_없을_때_피드백_저장_실패_테스트() {
        //given
        Long fakeId = 1L;
        String content = "피드백 내용입니다.";

        Student student = createStudent();
        ReflectionTestUtils.setField(student, "id", fakeId);

        // stub 1
        when(studentRepository.findById(anyLong())).thenReturn(Optional.of(student));
        when(teacherRepository.findById(anyLong())).thenReturn(Optional.empty());

        // stub 2
        Records records = Records.builder()
                .teacher(null)
                .student(student)
                .content(content)
                .build();
        ReflectionTestUtils.setField(records, "id", fakeId);

        // 1. 저장 요청
        RecordSaveReqDTO recordSaveReqDTO = RecordSaveReqDTO.builder()
                .studentId(fakeId)
                .teacherId(fakeId)
                .content(content)
                .build();

        //when
        //then
        assertThatThrownBy(() -> recordService.saveRecord(recordSaveReqDTO))
                .isInstanceOf(DataNotFoundException.class);

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

    @Test
    void 피드백_수정_실패_로직() {
        //given
        Long fakeId = 1L;
        String content = "수정된 피드백 입니다.";

        // stub
        Records record = Records.builder()
                .content(content)
                .build();
        ReflectionTestUtils.setField(record, "id", fakeId);

        when(recordsRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> recordService.modifyContent(record.getId(), content))
                .isInstanceOf(DataNotFoundException.class);
    }

    @Test
    void 피드백_삭제_테스트() {
        //given
        Long fakeId = 1L;
        String content = "수정된 피드백 입니다.";

        // stub
        Records record = Records.builder()
                .content(content)
                .status(RecordStatus.PUBLISHED)
                .build();

        ReflectionTestUtils.setField(record, "id", fakeId);

        when(recordsRepository.findById(anyLong())).thenReturn(Optional.of(record));

        //when
        recordService.deleteRecord(record.getId());

        //then
        assertThat(record.getStatus()).isEqualTo(RecordStatus.DELETED);
    }

    @Test
    void 특정_학생_아이디와_관련된_모든_피드백_조회() {
        // given
        Long fakeId = 1L;

        Student student = Student.builder()
                .name("철수")
                .build();
        ReflectionTestUtils.setField(student, "id", fakeId);

        // when
        Records record1 = Records.builder()
                .student(student)
                .content("피드백")
                .status(RecordStatus.PUBLISHED)
                .build();

        Records record2 = Records.builder()
                .student(student)
                .content("삭제된 피드백")
                .status(RecordStatus.DELETED)
                .build();

        ReflectionTestUtils.setField(record1, "id", fakeId);
        ReflectionTestUtils.setField(record2, "id", fakeId + 1L);

        when(studentRepository.findById(anyLong())).thenReturn(Optional.of(student));
        when(recordsRepository.findAllByStatusAndStudent(RecordStatus.PUBLISHED, student)).thenReturn(List.of(record1));

        // then
        assertThat(recordService.getStudentList(student.getId()).size()).isEqualTo(1);
    }

    @Test
    void 선생님이_작성한_모든_피드백_최신순으로_조회() throws InterruptedException {
        //given
        Long fakeId = 1L;

        Teacher teacher = Teacher.builder()
                .name("선생님")
                .build();
        ReflectionTestUtils.setField(teacher, "id", fakeId);

        Records oldRecord = Records.builder()
                .teacher(teacher)
                .content("Old 피드백")
                .build();

        Thread.sleep(2000); // 2초 뒤 저장

        Records newRecord = Records.builder()
                .teacher(teacher)
                .content("New 피드백")
                .build();

        ReflectionTestUtils.setField(oldRecord, "id", fakeId);
        ReflectionTestUtils.setField(newRecord, "id", fakeId + 1L);

        //when
        when(teacherRepository.findById(anyLong())).thenReturn(Optional.of(teacher));
        when(recordsRepository.findAllByTeacher(any())).thenReturn(List.of(newRecord, oldRecord));

        //then
        List<Records> result = recordService.getAllWrittenList(teacher.getId());
        assertThat(result.size()).isEqualTo(2);
        assertThat(result.get(0).getContent()).isEqualTo(newRecord.getContent());
        assertThat(result.get(1).getContent()).isEqualTo(oldRecord.getContent());
    }
}