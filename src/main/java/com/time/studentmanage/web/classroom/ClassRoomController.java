package com.time.studentmanage.web.classroom;

import com.time.studentmanage.domain.classroom.ClassRoom;
import com.time.studentmanage.domain.dto.classroom.ClassRoomInfoDto;
import com.time.studentmanage.domain.dto.classroom.ClassSaveReqDto;
import com.time.studentmanage.domain.dto.student.StudentRespDto;
import com.time.studentmanage.domain.member.Student;
import com.time.studentmanage.domain.member.Teacher;
import com.time.studentmanage.service.ClassRoomService;
import com.time.studentmanage.service.StudentService;
import com.time.studentmanage.web.login.SessionConst;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@Controller
@RequiredArgsConstructor
public class ClassRoomController {

    private final ClassRoomService classRoomService;
    private final StudentService studentService;

    @GetMapping("/class")
    public String showClassPage(HttpServletRequest request, Model model) {
        HttpSession session = request.getSession(false);
        Object loginSession = session.getAttribute(SessionConst.LOGIN_MEMBER_SESSION);

        if (session == null || loginSession == null) {
            return "redirect:/login";
        }
        if (!(loginSession instanceof Teacher teacher)) {
            return "redirect:/";
        }

        List<ClassRoomInfoDto> allTeacherClassRoomList = classRoomService.getAllTeacherClassRoom(teacher);

        model.addAttribute("classRoomList", allTeacherClassRoomList);

        return "classroom/class_list";
    }

    @GetMapping("/class/create")
    public String showClassCreateForm(@ModelAttribute("classSaveReqDto") ClassSaveReqDto classSaveReqDto, HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        Object loginSession = session.getAttribute(SessionConst.LOGIN_MEMBER_SESSION);

        if (session == null || loginSession == null) {
            return "redirect:/login";
        }
        if (!(loginSession instanceof Teacher)) {
            return "redirect:/";
        }

        return "classroom/class_create_form";
    }

    @PostMapping("/class/create")
    public String createClassRoom(@Validated @ModelAttribute("classSaveReqDto") ClassSaveReqDto classSaveReqDto, BindingResult bindingResult,
                                  HttpServletRequest request, Model model) {
        HttpSession session = request.getSession(false);
        Object loginSession = session.getAttribute(SessionConst.LOGIN_MEMBER_SESSION);

        if (session == null || loginSession == null) {
            return "redirect:/login";
        }
        if (!(loginSession instanceof Teacher)) {
            return "redirect:/";
        }

        if (bindingResult.hasErrors()) {
            return "classroom/class_create_form";
        }

        ClassRoom savedClassRoom = classRoomService.saveClassRoom(classSaveReqDto);

        String[] idBits = classSaveReqDto.getSelectedStudents().split(" ");
        for (String strId : idBits) {
            studentService.connectClassRoomById(Long.valueOf(strId), savedClassRoom);
        }

        return "redirect:/class";
    }
}
