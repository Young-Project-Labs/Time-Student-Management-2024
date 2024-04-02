package com.time.studentmanage.web.teacher;

import com.time.studentmanage.domain.dto.teacher.TeacherRespDto;
import com.time.studentmanage.domain.member.Teacher;
import com.time.studentmanage.service.TeacherService;
import com.time.studentmanage.web.login.SessionConst;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.List;

@Controller
@Slf4j
@RequiredArgsConstructor
public class TeacherController {
    private final TeacherService teacherService;

    //선생 목록 페이지
    @GetMapping("/teacher")
    public String teacherList(Model model, HttpSession session) {
        Object sessionObject = session.getAttribute(SessionConst.LOGIN_MEMBER_SESSION);
        //세션이 없는 경우 or 선생이 아닌 경우 redirect
        if (sessionObject == null || !sessionObject.getClass().equals(Teacher.class)) {
            return "redirect:/";
        }

        List<TeacherRespDto> teacherList = teacherService.getTeacherAllList();
        model.addAttribute("teacherList", teacherList);
        return "/teacher/teacher_list";
    }
}
