package com.time.studentmanage.service;

import com.time.studentmanage.domain.Answer;
import com.time.studentmanage.domain.Record;
import com.time.studentmanage.domain.dto.answer.AnswerSaveReqDTO;
import com.time.studentmanage.domain.dto.answer.AnswerUpdateReqDTO;
import com.time.studentmanage.domain.member.Teacher;
import com.time.studentmanage.repository.AnswerRepository;
import com.time.studentmanage.repository.RecordRepository;
import com.time.studentmanage.repository.TeacherRepository;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@Slf4j
@ExtendWith(MockitoExtension.class)
class AnswerServiceTest {

    @Mock
    RecordRepository recordRepository;
    @Mock
    TeacherRepository teacherRepository;
    @Mock
    AnswerRepository answerRepository;

    @InjectMocks
    AnswerService answerService;

    @Test
    void 피드백_댓글_생성() {
        //given
        Long fakeId = 1L;

        Teacher mockTeacher = Teacher.builder().build();
        Record mockRecord = Record.builder().build();
        Answer mockAnswer = Answer.builder().build();
        mockAnswer.addTeacher(mockTeacher);
        mockAnswer.addRecord(mockRecord);

        ReflectionTestUtils.setField(mockTeacher, "id", fakeId);
        ReflectionTestUtils.setField(mockRecord, "id", 2L);
        ReflectionTestUtils.setField(mockAnswer, "id", fakeId);

        AnswerSaveReqDTO answerSaveReqDTO = new AnswerSaveReqDTO();
        answerSaveReqDTO.setTeacherId(mockTeacher.getId());
        answerSaveReqDTO.setContent("댓글입니다.");
        answerSaveReqDTO.setRecordId(mockRecord.getId());

        when(teacherRepository.findById(mockTeacher.getId())).thenReturn(Optional.of(mockTeacher));
        when(recordRepository.findById(mockRecord.getId())).thenReturn(Optional.of(mockRecord));
        when(answerRepository.save(any())).thenReturn(mockAnswer);

        //when
        Long successId = answerService.createAnswer(answerSaveReqDTO);

        //then
        assertThat(successId).isEqualTo(mockAnswer.getId());

    }

    @Test
    void 피드백_댓글_수정_테스트() {
        //given
        Long fakeId = 1L;

        Teacher mockTeacher = Teacher.builder().build();
        Record mockRecord = Record.builder().build();
        Answer mockAnswer = Answer.builder()
                .content("댓글입니다.").build();
        mockAnswer.addTeacher(mockTeacher);
        mockAnswer.addRecord(mockRecord);

        ReflectionTestUtils.setField(mockTeacher, "id", fakeId);
        ReflectionTestUtils.setField(mockRecord, "id", 2L);
        ReflectionTestUtils.setField(mockAnswer, "id", fakeId);

        AnswerUpdateReqDTO answerUpdateReqDTO = new AnswerUpdateReqDTO();
        answerUpdateReqDTO.setAnswerId(mockAnswer.getId());
        answerUpdateReqDTO.setContent("수정된 댓글입니다.");

        when(answerRepository.findById(anyLong())).thenReturn(Optional.of(mockAnswer));

        //when
        answerService.updateAnswer(answerUpdateReqDTO);

        //then
        assertThat(mockAnswer.getContent()).isEqualTo("수정된 댓글입니다.");
    }


}