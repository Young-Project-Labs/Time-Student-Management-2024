package com.time.studentmanage.repository;

import com.time.studentmanage.domain.Address;
import com.time.studentmanage.domain.Answer;
import com.time.studentmanage.domain.Records;
import com.time.studentmanage.domain.enums.*;
import com.time.studentmanage.domain.member.Student;
import com.time.studentmanage.domain.member.Teacher;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.time.studentmanage.TestUtil.*;
import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@DataJpaTest
@Transactional
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class AnswerRepositoryTest {

    @Autowired
    AnswerRepository answerRepository;

    @Test
    @Order(1)
    void 생성된_피드백에_댓글_생성_및_조회() {
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

    @Test
    @Order(2)
    void 생성된_피드백의_댓글_수정() {
        //given
        Answer answer = createOneAnswer();
        Answer savedAnswer = answerRepository.save(answer);
        Answer findAnswer = answerRepository.findById(savedAnswer.getId()).get();

        //when
        findAnswer.changeAnswer("수정된 댓글 입니다.");
        answerRepository.flush();

        //then
        Answer updatedAnswer = answerRepository.findById(findAnswer.getId()).get();
        assertThat(updatedAnswer.getContent()).isEqualTo(findAnswer.getContent());
    }

    @Test
    @Order(3)
    void 생성된_피드백의_댓글_삭제() {
        //given
        Answer answer = createOneAnswer();
        Answer savedAnswer = answerRepository.save(answer);
        Answer findAnswer = answerRepository.findById(savedAnswer.getId()).get();

        //when
        findAnswer.deleteAnswer();
//        answerRepository.delete(findAnswer); // 진짜 삭제 x -> 삭제 처리만 하기
        answerRepository.flush();

        //then
        Answer deletedAnswer = answerRepository.findById(findAnswer.getId()).get();
        assertThat(deletedAnswer.getStatus()).isEqualTo(AnswerStatus.DELETED);
    }

    @Test
    @Order(4)
    void 생성된_댓글에_대댓글_작성() {
        //given
        Student student = createStudent();
        Teacher teacher = createTeacher();
        Records record = createRecord(teacher, student);

        // 부모 댓글 저장
        Answer savedParentAnswer = saveParentAnswer(record, teacher);

        // 자식 댓글 저장
        for (int i = 0; i < 5; i++) {
            Answer replyAnswer = new Answer(record, teacher, "대댓글입니다." + i, AnswerStatus.GENERAL);
            replyAnswer.addParentAnswer(savedParentAnswer);
            answerRepository.save(replyAnswer);
        }

        //when
        Answer findParentAnswer = answerRepository.findById(savedParentAnswer.getId()).get();

        //then
        assertThat(findParentAnswer.getContent()).isEqualTo(savedParentAnswer.getContent());
        assertThat(findParentAnswer.getChildAnswerList().size()).isEqualTo(5);

        String findChildContent = findParentAnswer.getChildAnswerList().get(0).getContent();
        String savedChildContent = savedParentAnswer.getChildAnswerList().get(0).getContent();
        assertThat(findChildContent).isEqualTo(savedChildContent);
    }


    @Test
    @Order(5)
    void 대댓글_수정() {
        //given
        Student student = createStudent();
        Teacher teacher = createTeacher();
        Records record = createRecord(teacher, student);

        // 부모 댓글 저장
        Answer savedParentAnswer = saveParentAnswer(record, teacher);
        // 자식 댓글 저장
        Answer savedReply = saveFirstChildReply(record, teacher, savedParentAnswer);

        //when
        Answer findReply = answerRepository.findById(savedReply.getId()).get();
        findReply.changeAnswer("수정된 대댓글입니다.");
        answerRepository.flush();

        Answer updatedReply = answerRepository.findById(findReply.getId()).get();

        //then
        assertThat(answerRepository.findById(updatedReply.getId()).get().getContent()).isEqualTo(findReply.getContent());
    }


    @Test
    @Order(6)
    void 생성된_부모댓글의_대댓글의_대댓글() {
        //given
        Student student = createStudent();
        Teacher teacher = createTeacher();
        Records record = createRecord(teacher, student);

        //when
        // 부모 댓글 저장
        Answer savedParentAnswer = saveParentAnswer(record, teacher);
        // 부모 댓글에 대한 대댓글
        Answer firstReplyAnswer = saveFirstChildReply(record, teacher, savedParentAnswer);
        // 대댓글에 대한 대댓글
        Answer savedReply2 = saveSecondChildReply(record, teacher, firstReplyAnswer);

        //then
        // 피드백에 대한 전체 댓글 수가 일치하는지
        assertThat(answerRepository.findAll().size()).isEqualTo(3);
        // 첫 대댓글의 부모가 첫 댓글인지
        Answer findAnswer = answerRepository.findById(firstReplyAnswer.getId()).get();
        assertThat(findAnswer.getParentAnswer().getId()).isEqualTo(savedParentAnswer.getId());
        // 대댓글의 대댓글 부모가 일치하는지
        Answer findAnswer2 = answerRepository.findById(savedReply2.getId()).get();
        assertThat(findAnswer2.getParentAnswer().getId()).isEqualTo(firstReplyAnswer.getId());
    }

    @Test
    @Order(7)
    void 대댓글의_부모_댓글_수정() {
        //given
        Student student = createStudent();
        Teacher teacher = createTeacher();
        Records record = createRecord(teacher, student);

        Answer savedParentAnswer = saveParentAnswer(record, teacher);
        Answer firstChildReply = saveFirstChildReply(record, teacher, savedParentAnswer);
        Answer secondReplyAnswer = saveSecondChildReply(record, teacher, firstChildReply);

        //when
        firstChildReply.changeAnswer("수정된 부모 대댓글");
        answerRepository.flush();

        // then
        Answer findParentAnswer = answerRepository.findById(savedParentAnswer.getId()).get();
        assertThat(findParentAnswer.getChildAnswerList().get(0).getContent()).isEqualTo(firstChildReply.getContent());

        Answer findReply = answerRepository.findById(secondReplyAnswer.getId()).get();
        assertThat(findReply.getParentAnswer().getContent()).isEqualTo(firstChildReply.getContent());
    }

    @Test
    @Order(8)
    void 대댓글의_부모_댓글_삭제() {
        //given
        Student student = createStudent();
        Teacher teacher = createTeacher();
        Records record = createRecord(teacher, student);

        Answer savedParentAnswer = saveParentAnswer(record, teacher);
        Answer firstChildReply = saveFirstChildReply(record, teacher, savedParentAnswer);
        Answer secondReplyAnswer = saveSecondChildReply(record, teacher, firstChildReply);

        //when
        firstChildReply.deleteAnswer();

        Answer findParentAnswer = answerRepository.findById(savedParentAnswer.getId()).get();
        assertThat(findParentAnswer.getChildAnswerList().get(0).getStatus()).isEqualTo(AnswerStatus.DELETED);
        Answer findReply = answerRepository.findById(secondReplyAnswer.getId()).get();
        assertThat(findReply.getParentAnswer().getStatus()).isEqualTo(AnswerStatus.DELETED);
    }

    @Test
    @Order(9)
    void 선생님이_작성한_모든_댓글_최신_날짜순으로_조회() throws InterruptedException {
        //given
        Student student = createStudent();
        Teacher teacher = createTeacher();
        Records record = createRecord(teacher, student);

        Answer answer = new Answer(record, teacher, "1 번째 피드백 댓글입니다.", AnswerStatus.GENERAL);
        answerRepository.save(answer);

        Thread.sleep(1000); // 2초 늦게 저장
        Student student2 = Student.builder()
                .name("노진구")
                .userId("njk@time.com").password("1234")
                .phoneNumber("010-4444-5555").schoolName("용호중학교")
                .classType(ClassType.MIDDLE).grade(3)
                .memberType(MemberType.STUDENT).gender(GenderType.MALE)
                .address(new Address("반림동", "현대 아파트", "102-342"))
                .attendanceStatus(AttendanceStatus.Y)
                .build();
        Records record2 = createRecord(teacher, student2);
        Answer answer2 = new Answer(record2, teacher, "2번째 피드백 댓글 입니다.", AnswerStatus.GENERAL);
        Answer secondReply = answerRepository.save(answer2);

        //when
        List<Answer> findAnswerList = answerRepository.findAllByTeacherName(teacher.getName());

        //then
        assertThat(findAnswerList.size()).isEqualTo(2);
        assertThat(findAnswerList.get(0).getContent()).isEqualTo(secondReply.getContent());
    }

    private Answer saveParentAnswer(Records record, Teacher teacher) {
        Answer parentAnswer = new Answer(record, teacher, "부모댓글입니다.", AnswerStatus.GENERAL);
        return answerRepository.save(parentAnswer);
    }

    private Answer saveFirstChildReply(Records record, Teacher teacher, Answer savedParentAnswer) {
        Answer replyAnswer = new Answer(record, teacher, "대댓글입니다.", AnswerStatus.GENERAL);
        replyAnswer.addParentAnswer(savedParentAnswer);
        return answerRepository.save(replyAnswer);
    }

    private Answer saveSecondChildReply(Records record, Teacher teacher, Answer firstReplyAnswer) {
        Answer replyAnswer = new Answer(record, teacher, "대댓글의 대댓글 입니다.", AnswerStatus.GENERAL);
        replyAnswer.addParentAnswer(firstReplyAnswer);
        return answerRepository.save(replyAnswer);
    }

}