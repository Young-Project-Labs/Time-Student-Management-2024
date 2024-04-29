package com.time.studentmanage.service;

import com.time.studentmanage.domain.record.Record;
import com.time.studentmanage.domain.dto.record.RecordRespDto;
import com.time.studentmanage.domain.dto.record.RecordSaveReqDto;
import com.time.studentmanage.domain.dto.record.RecordSearchDto;
import com.time.studentmanage.domain.enums.RecordStatus;
import com.time.studentmanage.domain.enums.SearchType;
import com.time.studentmanage.domain.member.Student;
import com.time.studentmanage.domain.member.Teacher;
import com.time.studentmanage.exception.DataNotFoundException;
import com.time.studentmanage.repository.student.StudentRepository;
import com.time.studentmanage.repository.teacher.TeacherRepository;
import com.time.studentmanage.repository.record.RecordRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.time.studentmanage.TestUtil.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.*;
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
                .content(content)
                .build();
        ReflectionTestUtils.setField(record, "id", fakeId);

        when(recordRepository.save(any())).thenReturn(record);

        // 1. 저장 요청
        RecordSaveReqDto recordSaveReqDTO = new RecordSaveReqDto(fakeId, fakeId, content);

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
                .content(content)
                .build();
        ReflectionTestUtils.setField(record, "id", fakeId);

//        when(recordsRepository.save(any())).thenReturn(record); // 테스트에서 사용되지 않는 스텁을 넣으면 테스트 오류 발생함

        // 1. 저장 요청
        RecordSaveReqDto recordSaveReqDTO = new RecordSaveReqDto(fakeId, fakeId, content);


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
        Record record = Record.builder()
                .teacher(null)
                .student(student)
                .content(content)
                .build();
        ReflectionTestUtils.setField(record, "id", fakeId);

        // 1. 저장 요청
        RecordSaveReqDto recordSaveReqDTO = new RecordSaveReqDto(fakeId, fakeId, content);

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
        Record record = createRecord(teacher, student);
        log.info("before save record={}", record.getContent());
        ReflectionTestUtils.setField(record, "id", fakeId);

        when(recordRepository.findById(anyLong())).thenReturn(Optional.of(record));

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
        Record record = Record.builder()
                .content(content)
                .build();
        ReflectionTestUtils.setField(record, "id", fakeId);

        when(recordRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> recordService.modifyContent(record.getId(), content))
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

    @Test
    void 특정_학생_아이디와_관련된_모든_피드백_조회() {
        // given
        Long fakeId = 1L;

        Student student = Student.builder()
                .name("철수")
                .build();
        ReflectionTestUtils.setField(student, "id", fakeId);

        Teacher teacher = createTeacher();
        ReflectionTestUtils.setField(teacher, "id", fakeId);

        // when
        Record record1 = Record.builder()
                .student(student)
                .teacher(teacher)
                .content("피드백")
                .status(RecordStatus.PUBLISHED)
                .build();

        Record record2 = Record.builder()
                .student(student)
                .teacher(teacher)
                .content("삭제된 피드백")
                .status(RecordStatus.DELETED)
                .build();

        ReflectionTestUtils.setField(record1, "id", fakeId);
        ReflectionTestUtils.setField(record2, "id", fakeId + 1L);

        when(studentRepository.findById(anyLong())).thenReturn(Optional.of(student));
        when(recordRepository.findAllByStatusAndStudent(RecordStatus.PUBLISHED, student)).thenReturn(List.of(record1));

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

        Student student = Student.builder()
                .name("김학생")
                .build();
        ReflectionTestUtils.setField(student, "id", fakeId);

        Record oldRecord = Record.builder()
                .teacher(teacher)
                .content("Old 피드백")
                .build();

        Thread.sleep(2000); // 2초 뒤 저장

        Record newRecord = Record.builder()
                .teacher(teacher)
                .content("New 피드백")
                .build();

        ReflectionTestUtils.setField(oldRecord, "id", fakeId);
        ReflectionTestUtils.setField(newRecord, "id", fakeId + 1L);

        oldRecord.addTeacher(teacher);
        newRecord.addTeacher(teacher);
        oldRecord.addStudent(student);
        newRecord.addStudent(student);

        //when
        when(teacherRepository.findById(anyLong())).thenReturn(Optional.of(teacher));
        when(recordRepository.findAllByTeacher(any())).thenReturn(List.of(newRecord, oldRecord));

        //then
        List<RecordRespDto> result = recordService.getAllWrittenList(teacher.getId());
        assertThat(result.size()).isEqualTo(2);
        assertThat(result.get(0).getContent()).isEqualTo(newRecord.getContent());
        assertThat(result.get(1).getContent()).isEqualTo(oldRecord.getContent());
    }

}