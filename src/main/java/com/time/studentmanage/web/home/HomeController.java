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

@Slf4j
@Controller
@RequiredArgsConstructor
public class HomeController {

    private final StudentService studentService;

    @GetMapping("/")
    public String home(HttpServletRequest request, Model model) {
        HttpSession session = request.getSession(false);

        if (session == null) {
            return "redirect:/login";
        }

        // TODO: 학생으로 로그인하면 학생 전용 페이지로 로딩, 선생님으로 로그인하면 선생님 전용 홈페이지로 이동
        // TODO: 학생/선생 전용 홈페이지 필요
        if (session.getAttribute(SessionConst.LOGIN_MEMBER_SESSION) instanceof Student) {
            Long id = ((Student) session.getAttribute(SessionConst.LOGIN_MEMBER_SESSION)).getId();
            log.info("student id ={}", id);
        } else if (session.getAttribute(SessionConst.LOGIN_MEMBER_SESSION) instanceof Teacher) {
            Long id = ((Teacher) session.getAttribute(SessionConst.LOGIN_MEMBER_SESSION)).getId();
            log.info("teacher id ={}", id);

            StudentSchoolListRespDto schoolNameRespDto = studentService.getAllSchoolName();

            model.addAttribute("elementarySchools", schoolNameRespDto.getElementarySchools());
            model.addAttribute("middleSchools", schoolNameRespDto.getMiddleSchools());
            model.addAttribute("highSchools", schoolNameRespDto.getHighSchools());
        }

        return "home";
    }
}
