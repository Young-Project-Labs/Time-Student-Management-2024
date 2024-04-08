package com.time.studentmanage.web.student;

import com.time.studentmanage.domain.dto.student.StudentRespDto;
import com.time.studentmanage.domain.dto.student.StudentSaveReqDto;
import com.time.studentmanage.domain.dto.student.StudentUpdateReqDto;
import com.time.studentmanage.domain.member.Student;
import com.time.studentmanage.service.StudentService;
import com.time.studentmanage.web.login.SessionConst;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@Slf4j
@RequiredArgsConstructor
public class StudentController {
    private final StudentService studentService;

    @GetMapping("/join")
    public String joinForm(@ModelAttribute("studentSaveReqDto") StudentSaveReqDto studentSaveReqDto, Model model) {
        return "/student/join_form";
    }

    @PostMapping("/join")
    public String joinStudent(@Valid @ModelAttribute StudentSaveReqDto studentSaveReqDto, BindingResult bindingResult, Model model) {
        log.info("studentSaveReqDto={}", studentSaveReqDto);

        if (bindingResult.hasErrors()) {
            model.addAttribute("studentSaveReqDto", studentSaveReqDto);
            return "/student/join_form";
        }

        studentService.saveStudent(studentSaveReqDto);
        return "redirect:/";
    }


    // 학생 개인 정보 수정 폼(마이페이지)
    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable("id") int id, HttpSession session, Model model) {
        Object sessionObject = session.getAttribute(SessionConst.LOGIN_MEMBER_SESSION);
        //세션이 없는 경우 진입X
        if (sessionObject == null) {
            return "redirect:/";
        }
        //세션에 저장된 id
        Long studentId = ((Student) sessionObject).getId();

        StudentRespDto studentRespDto = studentService.getStudentInfo(studentId);

        model.addAttribute("studentRespDto", studentRespDto);

        return "/student/edit_form";
    }

    /**
     * POST:학생 정보 수정
     */
    @PostMapping("/edit/{id}")
    public String editInfo(@PathVariable("id") int id, @ModelAttribute StudentRespDto studentRespDto, BindingResult bindingResult, HttpSession session) {
        Object sessionObject = session.getAttribute(SessionConst.LOGIN_MEMBER_SESSION);
        //세션이 없는 경우 진입X
        if (sessionObject == null) {
            return "redirect:/";
        }
        log.info("studentRespDto체크={}", studentRespDto);

        // studentRespDto -> studentUpdateDto
        StudentUpdateReqDto studentUpdateReqDto = new StudentUpdateReqDto(studentRespDto);
        log.info("studentUpdateReqDto 체크={}", studentUpdateReqDto);

        // edit
        studentService.updateStudentInfo(studentUpdateReqDto.getId(), studentUpdateReqDto);
        return "redirect:/";
    }


}
