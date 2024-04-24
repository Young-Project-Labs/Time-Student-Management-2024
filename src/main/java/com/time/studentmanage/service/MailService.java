package com.time.studentmanage.service;

import com.time.studentmanage.config.RedisUtil;
import com.time.studentmanage.domain.member.Student;
import com.time.studentmanage.domain.member.Teacher;
import com.time.studentmanage.exception.DataNotFoundException;
import com.time.studentmanage.exception.EmailAuthException;
import com.time.studentmanage.repository.student.StudentRepository;
import com.time.studentmanage.repository.teacher.TeacherRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Optional;
import java.util.Random;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class MailService {

    private final JavaMailSender emailSender;
    private final RedisUtil redisUtil;
    private final StudentRepository studentRepository;
    private final TeacherRepository teacherRepository;
    // 이메일 전송
    public void sendEmail(String toEmail,
                          String title,
                          String authNumber) {
        SimpleMailMessage emailForm = createEmailForm(toEmail, title, authNumber);
        //redis에 인증코드 저장(key : value) + 만료 시간 설정(300초)
        redisUtil.setDataExpire(authNumber,toEmail,60*5L);
        try {
            emailSender.send(emailForm);
        } catch (RuntimeException e) {
            log.debug("MailService.sendEmail exception occur toEmail: {}, " +
                    "title: {}, text: {}", toEmail, title, authNumber);
            throw new EmailAuthException("이메일 전송에 실패했습니다.");
        }
    }

    // 발신할 이메일 데이터 세팅
    private SimpleMailMessage createEmailForm(String toEmail,
                                              String title,
                                              String authNumber) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject(title);

        String content =
                "TIME 영어학원 인증코드 발송 메일입니다. " + 	//html 형식으로 작성 !
                        "인증 번호는 " + authNumber + "입니다.";
        message.setText(content);

        return message;
    }

    //메일 전송
    public void sendCodeToMail(String name, String toEmail) {
        //이메일 전송 전 DB에 등록된 이메일인지 검증한다. (학생, 선생 구별)
        if (toEmail.contains("@time.com")) {
            Optional<Teacher> teacherOP = teacherRepository.findByNameAndEmail(name, toEmail);
            if (!teacherOP.isPresent()) {
                throw new DataNotFoundException("존재하지 않는 이메일입니다.<br> 이름과 이메일을 확인해주세요.");
            }
        } else {
            Optional<Student> studentOP = studentRepository.findByNameAndEmail(name, toEmail);
            if (!studentOP.isPresent()) {
                throw new DataNotFoundException("존재하지 않는 이메일입니다.<br> 이름과 이메일을 확인해주세요.");
            }
        }

        String title = "TIME 영어학원 이메일 인증 번호입니다.";
        //인증 코드 생성
        String authCode = this.createCode();
        //이메일 전송
        sendEmail(toEmail, title, authCode);
    }

    //난수 생성
    private String createCode() {
        int lenth = 6;
        try {
            Random random = SecureRandom.getInstanceStrong();
            StringBuilder builder = new StringBuilder();
            for (int i = 0; i < lenth; i++) {
                builder.append(random.nextInt(10));
            }
            return builder.toString();
        } catch (NoSuchAlgorithmException e) {
            log.debug("MemberService.createCode() exception occur");
            throw new EmailAuthException("난수 생성 과정에서 예외 발생.");
        }
    }

    // 인증번호 체크
    public boolean checkAuthNum(String email,String authNum){
        log.info("checkAuthNum 인증번호 검증");
        if(redisUtil.getData(authNum)==null){
            // authCode가 틀리거나, null인 경우 발생하는 예외처리
            throw new EmailAuthException("인증번호가 일치하지 않습니다. 다시 입력해주세요.");
        }
        else if(redisUtil.getData(authNum).equals(email)){
            return true;
        }
        else{
            // authCode가 틀린 경우
            throw new EmailAuthException("인증번호가 일치하지 않습니다. 다시 입력해주세요.");
        }
    }
}
