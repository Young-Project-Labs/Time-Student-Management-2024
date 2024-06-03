package com.time.studentmanage.web.teacher;


import com.time.studentmanage.config.Auth;
import com.time.studentmanage.domain.dto.teacher.TeacherRespDto;
import com.time.studentmanage.domain.dto.teacher.TeacherSaveReqDto;
import com.time.studentmanage.domain.dto.teacher.TeacherSearchReqDto;
import com.time.studentmanage.domain.dto.teacher.TeacherUpdateReqDto;
import com.time.studentmanage.domain.enums.Position;
import com.time.studentmanage.domain.enums.SearchType;
import com.time.studentmanage.domain.member.Teacher;
import com.time.studentmanage.service.TeacherService;
import com.time.studentmanage.web.login.SessionConst;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Controller
@Slf4j
@RequiredArgsConstructor
public class TeacherController {
    private final TeacherService teacherService;

    @ModelAttribute("SearchTypes")
    public SearchType[] searchTypes() {
        return SearchType.values();
    }

    //선생 목록 페이지
    @Auth(role = {Auth.Role.CHIEF, Auth.Role.ADMIN})
    @GetMapping("/teacher")
    public String teacherList(@ModelAttribute("SearchReqDto") TeacherSearchReqDto searchReqDto,
                              Model model) {
        model.addAttribute("page", TeacherSearchReqDto.getPage());

        Page<TeacherRespDto> teacherList = teacherService.getTeacherList(searchReqDto);
        model.addAttribute("pagingResult", teacherList);
        return "teacher/teacher_list";
    }

    //선생 등록 페이지
    @Auth(role = {Auth.Role.CHIEF, Auth.Role.ADMIN})
    @GetMapping("/teacher/create")
    public String createForm(@ModelAttribute("teacherSaveReqDto") TeacherSaveReqDto teacherSaveReqDto) {
        return "teacher/teacher_create_form";
    }

    //선생 등록 로직
    @Auth(role = {Auth.Role.CHIEF, Auth.Role.ADMIN})
    @PostMapping("/teacher/create")
    public String createTeacher(@Validated @ModelAttribute TeacherSaveReqDto teacherSaveReqDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "teacher/teacher_create_form";
        }

        teacherService.createTeacher(teacherSaveReqDto);
        return "redirect:/teacher";
    }

    //선생 수정 폼
    @Auth(role = {Auth.Role.CHIEF, Auth.Role.ADMIN, Auth.Role.TEACHER})
    @GetMapping("/teacher/edit/{id}")
    public String editTeacherForm(@PathVariable(value = "id") Long id, Model model, HttpSession session) {
        //로그인 한 선생 권한 확인을 위해 변수 선언.
        Teacher loginTeacher = (Teacher) session.getAttribute(SessionConst.LOGIN_MEMBER_SESSION);

        // 선생 권한을 가진 경우 자신의 정보만 수정 가능.
        if (loginTeacher.getPosition().equals(Position.TEACHER) && loginTeacher.getId() != id) {
            throw new AccessDeniedException("접근할 수 없는 페이지입니다.");
        }

        TeacherRespDto teacherRespDto = teacherService.getTeacherInfo(id);

        model.addAttribute("teacherRespDto", teacherRespDto);

        //직급에 따라 폼을 별도로 리턴.
        if (loginTeacher.getPosition() == Position.TEACHER) {
            log.info("선생");
            return "teacher/teacher_edit_form";
        } else {
            log.info("관리자");
            return "teacher/teacher_edit_form_admin";
        }
    }

    //선생 수정 로직
    @Auth(role = {Auth.Role.CHIEF, Auth.Role.ADMIN, Auth.Role.TEACHER})
    @PostMapping("/teacher/edit/{id}")
    public String editTeacher(@PathVariable(value = "id") Long id, @Validated TeacherUpdateReqDto teacherUpdateReqDto, BindingResult bindingResult, HttpSession session) {
        if (bindingResult.hasErrors() || session == null) {
            return "teacher/teacher_edit_form";
        }

        //세션에 저장된 선생 정보
        Teacher loginTeacher = (Teacher) session.getAttribute(SessionConst.LOGIN_MEMBER_SESSION);
        // 선생 권한을 가진 경우 자신의 정보만 수정 가능.
        if (loginTeacher.getPosition().equals(Position.TEACHER) && loginTeacher.getId() != id) {
            throw new AccessDeniedException("잘못된 요청입니다.");
        }

        //선생 정보 수정
        Teacher teacher = teacherService.updateTeacherInfo(id, teacherUpdateReqDto);

        // 본인 정보 수정 시 세션에 저장된 값을 변경.
        if (loginTeacher.getId() == teacher.getId()) {
            //수정 후 세션에 저장된 값 변경
            session.setAttribute(SessionConst.LOGIN_MEMBER_SESSION, teacher);
        }
        return "redirect:/teacher/edit/" + id;

    }

    @Auth(role = {Auth.Role.CHIEF, Auth.Role.ADMIN})
    @DeleteMapping("/teacher/{id}")
    @ResponseBody
    public ResponseEntity<?> deleteTeacher(@PathVariable(value = "id") Long id) {
        teacherService.deleteTeacher(id);
        return ResponseEntity.ok("삭제에 성공했습니다.");
    }


}
