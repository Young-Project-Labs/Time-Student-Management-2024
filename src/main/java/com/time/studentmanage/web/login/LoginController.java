package com.time.studentmanage.web.login;

import com.time.studentmanage.domain.member.Student;
import com.time.studentmanage.service.LoginService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.tuple.CreationTimestampGeneration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.net.http.HttpRequest;
import java.util.Optional;

@Controller
@Slf4j
@RequiredArgsConstructor
public class LoginController {
    private final LoginService loginService;

    @GetMapping("/login")
    public String loginPage(@ModelAttribute("loginFormDto") LoginFormDto form, HttpSession session) {
        //세션이 있는 경우 진입X
        if (session.getAttribute(SessionConst.LOGIN_MEMBER_SESSION) != null) {
            log.info("sessionTest={}",session.getAttribute(SessionConst.LOGIN_MEMBER_SESSION).toString());
            return "redirect:/";
        }

        return "login/loginPage";
    }

    @PostMapping("/login")
    public String login(@Valid @ModelAttribute LoginFormDto form, BindingResult bindingResult, HttpServletRequest request) {
        //dto 검증
        if (bindingResult.hasErrors()) {
            log.error("error={}", bindingResult.getFieldError());
            return "login/loginPage";
        }
        //로그인 진행
        Optional<?> loginMemberOP = loginService.login(form.getLoginId(), form.getPassword(), form.getMemberType());

        //조회 실패(로그인 실패)
        if (!loginMemberOP.isPresent()) {
            bindingResult.reject("loginFail", "아이디 또는 비밀번호가 일치하지 않습니다.");
            return "login/loginPage";
        }

        //성공 시 세션 저장
        // true로 설정해야 세션이 없을 때 새로 생성함.
        HttpSession session = request.getSession(true);
        // 세션에 객체 저장
        session.setAttribute(SessionConst.LOGIN_MEMBER_SESSION,loginMemberOP.get());
        log.info("세션 저장함={}", session.getAttribute(SessionConst.LOGIN_MEMBER_SESSION));

        return "redirect:/";
    }

    @PostMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }
}
