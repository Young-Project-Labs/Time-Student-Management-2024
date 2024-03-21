package com.time.studentmanage.web.home;

import com.time.studentmanage.domain.member.Student;
import com.time.studentmanage.domain.member.Teacher;
import com.time.studentmanage.web.login.SessionConst;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.net.http.HttpRequest;

@Slf4j
@Controller
@RequiredArgsConstructor
public class HomeController {
    @GetMapping("/")
    public String home(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null) {
        } else {
            if (session.getAttribute(SessionConst.LOGIN_MEMBER_SESSION) instanceof Student) {
                Long id = ((Student) session.getAttribute(SessionConst.LOGIN_MEMBER_SESSION)).getId();
                log.info("student id ={}", id);
            } else if (session.getAttribute(SessionConst.LOGIN_MEMBER_SESSION) instanceof Teacher) {
                Long id = ((Teacher) session.getAttribute(SessionConst.LOGIN_MEMBER_SESSION)).getId();
                log.info("teacher id ={}", id);
            }
        }

        return "home";
    }
}
