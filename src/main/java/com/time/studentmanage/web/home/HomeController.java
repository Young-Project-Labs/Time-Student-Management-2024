package com.time.studentmanage.web.home;

import com.time.studentmanage.domain.dto.student.StudentSchoolListRespDto;
import com.time.studentmanage.domain.member.Student;
import com.time.studentmanage.domain.member.Teacher;
import com.time.studentmanage.service.StudentService;
import com.time.studentmanage.web.login.SessionConst;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
public class HomeController {

    private final StudentService studentService;

    @ModelAttribute("schools")
    public StudentSchoolListRespDto elementarySchoolList() {
        StudentSchoolListRespDto schoolNameRespDto = studentService.getAllSchoolName();
        return schoolNameRespDto;
    }

    @GetMapping("/")
    public String home(HttpServletRequest request) {
        HttpSession session = request.getSession();
            /**
             * 로그인한 사용자가 학생
             */
        if (session.getAttribute(SessionConst.LOGIN_MEMBER_SESSION) instanceof Student) {
            Long id = ((Student) session.getAttribute(SessionConst.LOGIN_MEMBER_SESSION)).getId();
            log.info("student id ={}", id);
            return "home_student";
            /**
             * 로그인한 사용자가 선생님
             */
        }
        if (session.getAttribute(SessionConst.LOGIN_MEMBER_SESSION) instanceof Teacher) {
            Long id = ((Teacher) session.getAttribute(SessionConst.LOGIN_MEMBER_SESSION)).getId();
            log.info("teacher id ={}", id);

            return "home_teacher";
        }

        // 세션이 없거나, 학생 및 선생님이 없는 경우 로그인 페이지로
        return "home_student";
    }
}
