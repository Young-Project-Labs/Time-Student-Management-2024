package com.time.studentmanage.web.login;

import com.time.studentmanage.domain.dto.student.KakaoAccount;
import com.time.studentmanage.domain.dto.student.KakaoSaveReqDto;
import com.time.studentmanage.domain.member.Student;
import com.time.studentmanage.exception.OAuthException;
import com.time.studentmanage.service.OAuthService;
import com.time.studentmanage.service.StudentService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import java.io.IOException;
import java.util.HashMap;
import java.util.Optional;

@Slf4j
@Controller
@RequiredArgsConstructor
public class OAuthController {

    private final OAuthService oAuthService;
    private final StudentService studentService;

    @Value("${kakao.client_id}")
    private String client_id;

    @Value("${kakao.redirect_uri}")
    private String redirect_uri;

    /**
     * 카카오 로그인 redirectUrI
     * @param code -> kakao Server에 Access Token 요청
     *
     */
    @GetMapping("/oauth/kakao")
    public String kakaoLogin(@RequestParam("code") String code, Model model, HttpServletRequest request) throws IOException {
        //1. 토큰 요청
        String kakaoAccessToken = oAuthService.getKakaoAccessToken(client_id, redirect_uri, code);
        log.info("kakaoAccessToken={}", kakaoAccessToken);

        //2. 카카오 서버에 사용자 정보 요청
        HashMap<String, Object> kakaoUserInfo = oAuthService.getKakaoUserInfo(kakaoAccessToken);

        if (kakaoUserInfo.isEmpty()) {
            log.info("사용자 이메일 정보가 없습니다.");
            throw new OAuthException("사용자 이메일 정보가 없습니다.");
        }

        log.info("사용자 정보 추출 kakaoUserInfo={}", kakaoUserInfo);



        /**
         * 카카오 로그인 시도
         * - 가입 여부 확인 (checkJoinKakaoStudent)
             - 가입 한 학생인 경우 로그인 진행 후 세션 저장
             - 미가입 학생인 경우 회원가입을 진행한 후 로그인
         *
         */
        //3. 가입 여부 확인 (checkJoinKakaoStudent)
        Optional<Student> studentOP = studentService.checkJoinKakaoStudent(kakaoUserInfo.get("email").toString());

        if (studentOP.isPresent()) {
            //3-1. 가입 한 경우 로그인 진행.
            log.info("이미 가입된 학생으로, 로그인을 진행합니다.");
            HttpSession session = request.getSession(true);
            // 세션에 객체 저장
            session.setAttribute(SessionConst.LOGIN_MEMBER_SESSION,studentOP.get());
        } else {
            //3-2. 미가입 한 경우 추가 정보 폼으로 이동.
            log.info("미가입 학생으로, 카카오 회원가입을 진행합니다.");
            KakaoAccount kakaoAccount = new KakaoAccount(kakaoUserInfo);
            KakaoSaveReqDto kakaoSaveReqDto = new KakaoSaveReqDto();
            kakaoSaveReqDto.setKakaoAccount(kakaoAccount);
            model.addAttribute("kakaoSaveReqDto", kakaoSaveReqDto);
            log.info("kakaoSaveReqDto={}", kakaoSaveReqDto.toString());
            return "student/kakao_join_form";
        }
        return "redirect:/";
    }

    // 폼으로 이동한 후 추가정보 입력 후 회원가입 진행
    @PostMapping("/oauth/kakao")
    public String kakaoJoinForm(@Valid @ModelAttribute KakaoSaveReqDto kakaoSaveReqDto, BindingResult bindingResult, Model model, HttpServletRequest request) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("kakaoSaveReqDto", kakaoSaveReqDto);
            return "student/kakao_join_form";
        }
        // 회원가입 진행
        log.info("카카오 회원가입 : kakaoSaveReqDto={}",kakaoSaveReqDto);
        studentService.saveStudentKaKao(kakaoSaveReqDto);

        // 회원가입을 정상적으로 처리한 경우 바로 로그인 진행.
        HttpSession session = request.getSession(true);
        // 세션에 객체 저장
        Optional<Student> loginStudentOP = studentService.checkJoinKakaoStudent(kakaoSaveReqDto.getKakaoAccount().getEmail());
        session.setAttribute(SessionConst.LOGIN_MEMBER_SESSION,loginStudentOP.get());
        log.info("회원가입을 완료 했으며, 로그인을 진행합니다.{}", session.getAttribute(SessionConst.LOGIN_MEMBER_SESSION));

        return "redirect:/";
    }

}
