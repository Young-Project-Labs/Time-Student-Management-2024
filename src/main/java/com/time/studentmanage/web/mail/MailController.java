package com.time.studentmanage.web.mail;

import com.time.studentmanage.domain.dto.student.AuthCheckDto;
import com.time.studentmanage.domain.dto.student.MailSendReqDto;
import com.time.studentmanage.domain.enums.MailSearchType;
import com.time.studentmanage.exception.ErrorResult;
import com.time.studentmanage.service.MailService;
import com.time.studentmanage.web.login.FindIdValidation;
import com.time.studentmanage.web.login.FindPwdValidation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.SmartValidator;
import org.springframework.validation.Validator;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@Slf4j
@RestController
@RequiredArgsConstructor
public class MailController {
    private final MailService mailService;
    private final MessageSource messageSource;
    private final SmartValidator validator;

    @InitBinder
    protected void initBinder(WebDataBinder binder) {
        binder.setValidator(validator);
    }
    // 이메일 전송
    @PostMapping("/mailSend")
    public ResponseEntity sendEmail(MailSendReqDto mailSendReqDto, BindingResult bindingResult, Locale locale) {
        log.info("mailSendReqDto={}", mailSendReqDto.toString());
        // 동적으로 판별하기 위해 Validator 인터페이스를 직접 사용
        if (mailSendReqDto.getSearchType().equals(MailSearchType.USERID)) { // 아이디 찾기
            validator.validate(mailSendReqDto, bindingResult, FindIdValidation.class);

        } else if (mailSendReqDto.getSearchType().equals(MailSearchType.PASSWORD)) { // 비밀번호 찾기
            validator.validate(mailSendReqDto, bindingResult, FindPwdValidation.class);
        }

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

        //메일 전송
        mailService.sendCodeToMail(mailSendReqDto);

        return ResponseEntity.ok("이메일 전송이 완료되었습니다.<br> 인증코드를 입력해주세요.");
    }

    //인증번호 검증 요청
    @PostMapping("/mailAuthCheck")
    public ResponseEntity authCheck(@Validated AuthCheckDto authCheckDto, BindingResult bindingResult, Locale locale) {
        log.info("authCheckDto={}", authCheckDto);
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
