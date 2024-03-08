package com.time.studentmanage.repository;

import com.time.studentmanage.domain.Answer;
import com.time.studentmanage.domain.Records;
import com.time.studentmanage.domain.enums.AnswerStatus;
import com.time.studentmanage.domain.member.Student;
import com.time.studentmanage.domain.member.Teacher;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.transaction.annotation.Transactional;

import static com.time.studentmanage.TestUtil.*;
import static org.assertj.core.api.Assertions.*;

@Slf4j
@DataJpaTest
@Transactional
class AnswerRepositoryTest {

    @Autowired
    AnswerRepository answerRepository;

    @Test
    void 생성된_피드백에_댓글_작성() {
        //given
        Answer answer = createOneAnswer();

        //when
        Answer savedAnswer = answerRepository.save(answer);
        Answer findAnswer = answerRepository.findById(savedAnswer.getId()).get();

        //then
        assertThat(findAnswer.getContent()).isEqualTo(savedAnswer.getContent());
        assertThat(findAnswer.getRecords().getAnswerList().size()).isEqualTo(1);
        assertThat(findAnswer.getTeacher().getName()).isEqualTo(savedAnswer.getTeacher().getName());
    }



}