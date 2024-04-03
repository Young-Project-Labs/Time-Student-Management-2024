package com.time.studentmanage.web.student;

import com.time.studentmanage.domain.dto.student.StudentRespDto;
import com.time.studentmanage.domain.dto.student.StudentSaveReqDto;
import com.time.studentmanage.domain.dto.student.StudentUpdateReqDto;
import com.time.studentmanage.domain.member.Student;
import com.time.studentmanage.service.StudentService;
import com.time.studentmanage.service.TeacherService;
import com.time.studentmanage.web.login.SessionConst;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@Slf4j
@RequiredArgsConstructor
public class StudentController {
    private final StudentService studentService;
    private final TeacherService teacherService;

    @GetMapping("/join")
    public String joinForm(@ModelAttribute("studentSaveReqDto") StudentSaveReqDto studentSaveReqDto, Model model) {
        return "/student/joinForm";
    }

    @PostMapping("/join")
    public String joinStudent(@Valid @ModelAttribute StudentSaveReqDto studentSaveReqDto, BindingResult bindingResult, Model model) {
        log.info("studentSaveReqDto={}", studentSaveReqDto);

        if (bindingResult.hasErrors()) {
            model.addAttribute("studentSaveReqDto", studentSaveReqDto);
            return "/student/joinForm";
        }

        studentService.saveStudent(studentSaveReqDto);
        return "redirect:/";
    }

    // 학생 개인 정보 수정(마이페이지)
    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable("id") int id, HttpSession session, Model model) {
        Object sessionObject = session.getAttribute(SessionConst.LOGIN_MEMBER_SESSION);
        //세션이 없는 경우 진입X
        if (sessionObject == null) {
            return "redirect:/";
        }

        /**
         * MemberType에 따른 분기
         *
         */
        //학생
        Long studentId = ((Student) sessionObject).getId();
        StudentRespDto studentRespDto = studentService.getStudentInfo(studentId);
        model.addAttribute("studentRespDto", studentRespDto);

        //선생
//        } else if (sessionObject instanceof Teacher) {
//            Long teacherId = ((Teacher) sessionObject).getId();
//            TeacherRespDto teacherInfo = teacherService.getTeacherInfo(teacherId);
//            model.addAttribute("teacherInfo", teacherInfo);
//
//        }
        return "/student/editForm";
    }


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
        StudentRespDto updateRespDto = studentService.updateStudentInfo(studentUpdateReqDto.getId(), studentUpdateReqDto);
        return "redirect:/";
    }

    @GetMapping("/school")
    public String showStudentList(@RequestParam(value = "schoolName", required = false) String schoolName, Model model) {
        List<StudentRespDto> studentList = studentService.getAllStudentsBySchoolName(schoolName);
        model.addAttribute("studentList", studentList);

        return "student/student_list";
    }

    @PostMapping("/school")
    public String searchStudent(@RequestParam(value = "content") String content, Model model) {
        log.info("content={}", content);

        List<StudentRespDto> studentList = studentService.getSearchedStudent("%" + content + "%");
        model.addAttribute("studentList", studentList);

        return "student/student_list";
    }
}
