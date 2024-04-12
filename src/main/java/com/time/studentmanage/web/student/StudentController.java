package com.time.studentmanage.web.student;

import com.time.studentmanage.domain.dto.student.StudentRespDto;
import com.time.studentmanage.domain.dto.student.StudentSaveReqDto;
import com.time.studentmanage.domain.dto.student.StudentUpdateReqDto;
import com.time.studentmanage.domain.member.Student;
import com.time.studentmanage.service.StudentService;
import com.time.studentmanage.web.login.SessionConst;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@Slf4j
@RequiredArgsConstructor
public class StudentController {
    private final StudentService studentService;

    /**
     * 학생 목록
     * - 선생 타입인 경우에만 접근할 수 있음.
     * - 학생 정보 수정 및 삭제가 필요할 때
     * - 학생 현황 파악에도 사용
     * - 페이징 처리 필요
     *
     */
    @GetMapping("/student")
    public String student_list_admin(HttpSession session, Model model) {
        //학생이거나 혹은 세션이 없는 경우 접근 X
        if (session.getAttribute(SessionConst.LOGIN_MEMBER_SESSION) == null || session.getAttribute(SessionConst.LOGIN_MEMBER_SESSION).getClass().equals(Student.class)) {
            return "redirect:/";
        }

        List<StudentRespDto> studentList = studentService.getAllStudent();
        model.addAttribute("studentList", studentList);

        return "/student/student_list_admin";
    }

    @GetMapping("/join")
    public String joinForm(@ModelAttribute("studentSaveReqDto") StudentSaveReqDto studentSaveReqDto, Model model) {
        return "/student/join_form";
    }

    @PostMapping("/join")
    public String joinStudent(@Validated @ModelAttribute StudentSaveReqDto studentSaveReqDto, BindingResult bindingResult,
            Model model) {
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
    public String editForm(@PathVariable("id") Long id, HttpSession session, Model model) {
        Object sessionObject = session.getAttribute(SessionConst.LOGIN_MEMBER_SESSION);
        // 세션이 없는 경우 진입X
        if (sessionObject == null) {
            return "redirect:/";
        }

        StudentRespDto studentRespDto = studentService.getStudentInfo(id);

        model.addAttribute("studentRespDto", studentRespDto);

        return "/student/edit_form";
    }

    /**
     * POST:학생 정보 수정
     */
    @PostMapping("/edit/{id}")
    public String editInfo(@PathVariable("id") int id, @Validated @ModelAttribute StudentRespDto studentRespDto,
            BindingResult bindingResult, HttpSession session, Model model) {
        Object sessionObject = session.getAttribute(SessionConst.LOGIN_MEMBER_SESSION);
        // 세션이 없는 경우 진입X
        if (sessionObject == null) {
            return "redirect:/";
        }

        if (bindingResult.hasErrors()) {
            model.addAttribute("studentRespDto", studentRespDto);
            return "/student/edit_form";
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
