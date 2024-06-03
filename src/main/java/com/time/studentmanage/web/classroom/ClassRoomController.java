package com.time.studentmanage.web.classroom;

import com.time.studentmanage.config.Auth;
import com.time.studentmanage.domain.classroom.ClassRoom;
import com.time.studentmanage.domain.dto.classroom.*;
import com.time.studentmanage.domain.enums.SearchType;
import com.time.studentmanage.domain.member.Student;
import com.time.studentmanage.domain.member.Teacher;
import com.time.studentmanage.service.ClassRoomService;
import com.time.studentmanage.service.StudentService;
import com.time.studentmanage.web.login.SessionConst;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Controller
@RequiredArgsConstructor
public class ClassRoomController {

    private final ClassRoomService classRoomService;
    private final StudentService studentService;

    @Auth(role = {Auth.Role.TEACHER, Auth.Role.ADMIN, Auth.Role.CHIEF})
    @ModelAttribute("searchTypeOptions")
    public SearchType[] searchType() {
        SearchType[] filteredSearchTypes = Arrays.stream(SearchType.values())
                .filter(type -> type == SearchType.CLASS_NAME ||
                        type == SearchType.CLASS_INFO)
                .collect(Collectors.toList())
                .toArray(new SearchType[0]); // 배열 타입을 알려주기 위함

        return filteredSearchTypes;
    }

    @Auth(role = {Auth.Role.TEACHER, Auth.Role.ADMIN, Auth.Role.CHIEF})
    @GetMapping("/class/{teacherId}")
    public String showClassPage(@ModelAttribute("classRoomSearchReqDto") ClassRoomSearchReqDto classRoomSearchReqDto,
                                @RequestParam(value = "page", defaultValue = "0") int page,
                                HttpServletRequest request,
                                Model model) {

        Teacher teacher = (Teacher) request.getSession(false).getAttribute(SessionConst.LOGIN_MEMBER_SESSION);

        Page<ClassRoomRespDto> pagingResult = classRoomService.getAllTeacherClassRoom(teacher, page);
        model.addAttribute("classRoomSearchReqDto", classRoomSearchReqDto);
        model.addAttribute("pagingResult", pagingResult);

        return "classroom/class_list";
    }

    @Auth(role = {Auth.Role.TEACHER, Auth.Role.ADMIN, Auth.Role.CHIEF})
    @GetMapping("/class/list/{teacherId}")
    public String updateClassPage(@ModelAttribute("classRoomSearchReqDto") ClassRoomSearchReqDto classRoomSearchReqDto,
                                  @PathVariable("teacherId") Long teacherId,
                                  Model model) {
        Page<ClassRoomRespDto> pagingResult = classRoomService.getPageUpdateResult(teacherId, classRoomSearchReqDto.getSearchType(), classRoomSearchReqDto.getContent(), classRoomSearchReqDto.getPage());
        model.addAttribute("classRoomSearchReqDto", classRoomSearchReqDto);
        model.addAttribute("pagingResult", pagingResult);

        return "classroom/class_list";
    }

    @Auth(role = {Auth.Role.TEACHER, Auth.Role.ADMIN, Auth.Role.CHIEF})
    @GetMapping("/class/create")
    public String showClassCreateForm(@ModelAttribute("classSaveReqDto") ClassSaveReqDto classSaveReqDto) {

        return "classroom/class_create_form";
    }

    @Auth(role = {Auth.Role.TEACHER, Auth.Role.ADMIN, Auth.Role.CHIEF})
    @PostMapping("/class/create")
    public String createClassRoom(@Validated @ModelAttribute("classSaveReqDto") ClassSaveReqDto classSaveReqDto, BindingResult bindingResult,
                                  @RequestParam("teacherId") Long teacherId) {

        if (bindingResult.hasErrors()) {
            return "classroom/class_create_form";
        }

        ClassRoom savedClassRoom = classRoomService.saveClassRoom(classSaveReqDto);

        String[] idBits = classSaveReqDto.getSelectedStudents().split(" ");
        for (String strId : idBits) {
            studentService.connectClassRoom(Long.valueOf(strId), savedClassRoom);
        }

        return "redirect:/class/" + teacherId;
    }

    @Auth(role = {Auth.Role.TEACHER, Auth.Role.ADMIN, Auth.Role.CHIEF})
    @GetMapping("/class/{id}/basic/info")
    public String showBasicInfoEditForm(@PathVariable("id") Long id, Model model) {
        ClassRoomBasicInfoDto basicClassRoomInfoDto = classRoomService.getBasicClassRoomInfo(id);

        model.addAttribute("basicClassRoomInfoDto", basicClassRoomInfoDto);
        return "classroom/class_basic_info_edit_form";
    }

    @Auth(role = {Auth.Role.TEACHER, Auth.Role.ADMIN, Auth.Role.CHIEF})
    @PostMapping("/class/{id}/basic/info")
    public String updateBasicInfo(@PathVariable("id") Long id,
                                  @RequestParam("teacherId") Long teacherId,
                                  @ModelAttribute("basicClassRoomInfoDto") ClassRoomBasicInfoDto classRoomBasicInfoDto) {

        classRoomService.updateClassRoomBasicInfo(id, classRoomBasicInfoDto);

        return "redirect:/class/" + teacherId;
    }

    @Auth(role = {Auth.Role.TEACHER, Auth.Role.ADMIN, Auth.Role.CHIEF})
    @GetMapping("/class/{classId}/student/info")
    public String showClassStudentManagePage(@PathVariable("classId") Long id, Model model) {
        List<ClassStudentRespDto> classStudentList = classRoomService.getClassStudentList(id);

        model.addAttribute("classStudentList", classStudentList);
        return "classroom/class_student";
    }

    @Auth(role = {Auth.Role.TEACHER, Auth.Role.ADMIN, Auth.Role.CHIEF})
    @PostMapping("/class/{classId}/delete/student")
    public String deleteClassStudent(@PathVariable("classId") Long classId,
                                     @RequestParam("studentId") Long studentId) {
        studentService.disconnectClassRoom(studentId);
        return "redirect:/class/" + classId + "/student/info";
    }

    @Auth(role = {Auth.Role.TEACHER, Auth.Role.ADMIN, Auth.Role.CHIEF})
    @PostMapping("/class/delete/{id}")
    public String deleteClassRoom(@PathVariable("id") Long id, @RequestParam("teacherId") Long teacherId) {
        ClassRoom classRoom = classRoomService.findById(id);

        for (Student student : classRoom.getStudentList()) {
            studentService.disconnectClassRoom(student.getId());
        }

        classRoomService.deleteClassRoom(id);
        return "redirect:/class/" + teacherId;

    }
}
