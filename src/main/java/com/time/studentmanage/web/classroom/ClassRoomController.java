package com.time.studentmanage.web.classroom;

import com.time.studentmanage.domain.classroom.ClassRoom;
import com.time.studentmanage.domain.dto.classroom.ClassRoomBasicInfoDto;
import com.time.studentmanage.domain.dto.classroom.ClassRoomInfoDto;
import com.time.studentmanage.domain.dto.classroom.ClassSaveReqDto;
import com.time.studentmanage.domain.dto.classroom.ClassStudentRespDto;
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
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
            studentService.connectClassRoom(Long.valueOf(strId), savedClassRoom);
        }

        return "redirect:/class";
    }

    @GetMapping("/class/{id}/basic/info")
    public String showBasicInfoEditForm(@PathVariable("id") Long id, Model model) {
        ClassRoomBasicInfoDto basicClassRoomInfoDto = classRoomService.getBasicClassRoomInfo(id);

        model.addAttribute("basicClassRoomInfoDto", basicClassRoomInfoDto);
        return "classroom/class_basic_info_edit_form";
    }

    @PostMapping("/class/{id}/basic/info")
    public String updateBasicInfo(@PathVariable("id") Long id, @ModelAttribute("basicClassRoomInfoDto") ClassRoomBasicInfoDto classRoomBasicInfoDto) {

        classRoomService.updateClassRoomBasicInfo(id, classRoomBasicInfoDto);

        return "redirect:/class";
    }

    @GetMapping("/class/{classId}/student/info")
    public String showClassStudentManagePage(@PathVariable("classId") Long id, Model model) {
        List<ClassStudentRespDto> classStudentList = classRoomService.getClassStudentList(id);

        model.addAttribute("classStudentList", classStudentList);
        return "classroom/class_student";
    }

    @GetMapping("/class/{classId}/delete/student")
    public String deleteClassStudent(@PathVariable("classId") Long classId,
                                     @RequestParam("studentId") Long studentId) {
        ClassRoom classRoom = classRoomService.findById(classId);
        studentService.disconnectClassRoom(studentId, classRoom);

        return "redirect:/class/" + classId + "/student/info";
    }

}
