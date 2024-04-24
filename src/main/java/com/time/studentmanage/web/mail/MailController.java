package com.time.studentmanage.web.mail;

import com.time.studentmanage.domain.dto.student.AuthCheckDto;
import com.time.studentmanage.domain.dto.student.MailSendReqDto;
import com.time.studentmanage.exception.EmailAuthException;
import com.time.studentmanage.exception.ErrorResult;
import com.time.studentmanage.service.MailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@Slf4j
@RestController
@RequiredArgsConstructor
public class MailController {
    private final MailService mailService;
    private final MessageSource messageSource;

    // 이메일 전송
    @PostMapping("/mailSend")
    public ResponseEntity sendEmail(@Validated MailSendReqDto mailSendReqDto, BindingResult bindingResult, Locale locale) {
        if (bindingResult.hasErrors()) {
            log.info("bindingResult={}", bindingResult);
            List<ErrorResult> errorResultList = new ArrayList<>();

            for (FieldError error : bindingResult.getFieldErrors()) {
                String errorMessage = messageSource.getMessage(error, locale);
                ErrorResult errorResult = new ErrorResult(error.getField(), errorMessage);
                errorResultList.add(errorResult);
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(errorResultList);
        }

        //형식 검증 후 메일 전송
        mailService.sendCodeToMail(mailSendReqDto.getName(), mailSendReqDto.getEmail());
        return ResponseEntity.ok("이메일 전송이 완료되었습니다.<br> 인증코드를 입력해주세요.");
    }

    //인증번호 검증 요청
    @PostMapping("/mailAuthCheck")
    public ResponseEntity authCheck(@Validated AuthCheckDto authCheckDto, BindingResult bindingResult, Locale locale) {
        log.info("authCheckDto={}", authCheckDto);
        //TODO: model에 값이 담겨있지 않을 때 사용자에게 보여지는 부분 처리 필요
        if (bindingResult.hasErrors()) {
            log.info("bindingResult={}", bindingResult);
            List<ErrorResult> errorResultList = new ArrayList<>();

            for (FieldError error : bindingResult.getFieldErrors()) {
                String errorMessage = messageSource.getMessage(error, locale);
                errorResultList.add(new ErrorResult(error.getField(), errorMessage));
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(errorResultList);
        }

        //인증번호 검증
        mailService.checkAuthNum(authCheckDto.getEmail(), authCheckDto.getAuthCode());
        return ResponseEntity.ok("인증이 완료되었습니다.");
    }


}
