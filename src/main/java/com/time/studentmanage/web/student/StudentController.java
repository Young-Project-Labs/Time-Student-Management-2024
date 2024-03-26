package com.time.studentmanage.web.student;

import com.time.studentmanage.domain.dto.student.StudentSaveReqDto;
import com.time.studentmanage.domain.enums.GenderType;
import com.time.studentmanage.service.StudentService;
import jakarta.validation.Valid;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@Slf4j
@RequiredArgsConstructor
public class StudentController {
    private final StudentService studentService;

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


}
