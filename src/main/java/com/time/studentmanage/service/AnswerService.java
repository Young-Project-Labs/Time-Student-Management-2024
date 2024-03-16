package com.time.studentmanage.service;

import com.time.studentmanage.domain.Answer;
import com.time.studentmanage.domain.Record;
import com.time.studentmanage.domain.dto.answer.AnswerRespDTO;
import com.time.studentmanage.domain.dto.answer.AnswerSaveReqDTO;
import com.time.studentmanage.domain.dto.answer.AnswerUpdateReqDTO;
import com.time.studentmanage.domain.member.Teacher;
import com.time.studentmanage.exception.DataNotFoundException;
import com.time.studentmanage.repository.AnswerRepository;
import com.time.studentmanage.repository.RecordRepository;
import com.time.studentmanage.repository.TeacherRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class AnswerService {

    private final AnswerRepository answerRepository;
    private final TeacherRepository teacherRepository;
    private final RecordRepository recordRepository;

    public Long createAnswer(AnswerSaveReqDTO answerSaveReqDTO) {
        Optional<Teacher> teacherOP = teacherRepository.findById(answerSaveReqDTO.getTeacherId());
        Optional<Record> recordOP = recordRepository.findById(answerSaveReqDTO.getRecordId());

        if (!recordOP.isPresent()) {
            throw new DataNotFoundException("존재하지 않는 피드백 정보 입니다.");
        }

        if (!teacherOP.isPresent()) {
            throw new DataNotFoundException("존재하지 않는 선생님 정보 입니다.");
        }

        Teacher teacherPS = teacherOP.get();
        Record recordPS = recordOP.get();

        Answer answerNP = answerSaveReqDTO.toEntity(); // 아직 영속화 x (Not Persist)

        if (answerSaveReqDTO.getParentAnswerId() != null) {
            Answer parentAnswer = getParentAnswer(answerSaveReqDTO.getParentAnswerId());
            answerNP.addParentAnswer(parentAnswer);
        }
        answerNP.addTeacher(teacherPS);
        answerNP.addRecord(recordPS);

        Answer savedAnswer = answerRepository.save(answerNP);
        return savedAnswer.getId();
    }

    private Answer getParentAnswer(Long id) {
        Optional<Answer> answerOP = answerRepository.findById(id);
        if (!answerOP.isPresent()) {
            throw new DataNotFoundException("존재하지 않는 댓글 정보입니다.");
        }
        return answerOP.get();
    }

    public void updateAnswer(AnswerUpdateReqDTO answerUpdateReqDTO) {
        Answer answer = answerRepository.findById(answerUpdateReqDTO.getAnswerId())
                .orElseThrow(() -> new DataNotFoundException("존재하지 않는 댓글 정보 입니다."));

        answer.changeAnswer(answerUpdateReqDTO.getContent());
    }
}
