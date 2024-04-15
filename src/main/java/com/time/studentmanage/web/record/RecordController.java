package com.time.studentmanage.web.record;

import com.time.studentmanage.domain.dto.record.RecordRespDto;
import com.time.studentmanage.domain.dto.record.RecordSaveReqDto;
import com.time.studentmanage.domain.dto.record.RecordSearchDto;
import com.time.studentmanage.domain.dto.record.RecordUpdateReqDto;
import com.time.studentmanage.domain.dto.student.StudentRespDto;
import com.time.studentmanage.domain.enums.SearchType;
import com.time.studentmanage.domain.member.Teacher;
import com.time.studentmanage.exception.DataNotFoundException;
import com.time.studentmanage.service.RecordService;
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
public class RecordController {

    private final RecordService recordService;
    private final StudentService studentService;

    @ModelAttribute("searchTypes")
    public SearchType[] searchType() {
        return SearchType.values();
    }

    @GetMapping("/record/{studentId}")
    public String records(@PathVariable("studentId") Long id, HttpServletRequest request, Model model) {
        HttpSession session = request.getSession(false);
        Object loginSession = session.getAttribute(SessionConst.LOGIN_MEMBER_SESSION);

        if (session == null || loginSession == null) {
            return "redirect:/login";
        }

        // 선생님으로 로그인한 것이 아니라면 홈페이지로 redirect
        if (!(loginSession instanceof Teacher)) {
            return "redirect:/";
        }

        StudentRespDto studentRespDto = studentService.getStudentInfo(id);
        List<RecordRespDto> recordList = recordService.getStudentList(id);

        RecordSearchDto recordSearchDto = new RecordSearchDto();
        recordSearchDto.setStudentName(studentRespDto.getName());

        model.addAttribute("recordSearchDto", recordSearchDto);
        model.addAttribute("recordList", recordList);

        return "record/record_list";
    }

    @PostMapping("/record/{studentId}")
    public String filterRecords(@Validated @ModelAttribute RecordSearchDto recordSearchDto, BindingResult result, @PathVariable("studentId") Long studentId, HttpServletRequest request, Model model) {

        HttpSession session = request.getSession(false);
        Object loginSession = session.getAttribute(SessionConst.LOGIN_MEMBER_SESSION);

        if (session == null || loginSession == null) {
            return "redirect:/login";
        }

        // 선생님으로 로그인한 것이 아니라면 홈페이지로 redirect
        if (!(loginSession instanceof Teacher teacher)) {
            return "redirect:/";
        }

        if (result.hasErrors()) {
            log.info("errors={}", result);
            List<RecordRespDto> recordList = recordService.getStudentList(studentId);
            model.addAttribute("recordList", recordList);
            model.addAttribute("recordSearchDto", recordSearchDto);
            return "record/record_list";
        }

        recordSearchDto.setTeacherId(teacher.getId());

        StudentRespDto studentRespDto = studentService.getStudentInfo(studentId);
        List<RecordRespDto> recordList = recordService.getFilteredResults(recordSearchDto);

        model.addAttribute("recordList", recordList);
        model.addAttribute("studentName", studentRespDto.getName());
        model.addAttribute("recordSearchDto", recordSearchDto);

        return "record/record_list";
    }

    @GetMapping("/record/create")
    public String showCreateRecordForm(@RequestParam(value = "studentId", required = false) Long studentId, Model model, HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        Object loginSession = session.getAttribute(SessionConst.LOGIN_MEMBER_SESSION);

        if (session == null || loginSession == null) {
            return "redirect:/login";
        }

        // 선생님으로 로그인한 것이 아니라면 홈페이지로 redirect
        if (!(loginSession instanceof Teacher teacher)) {
            return "redirect:/";
        }

        Long teacherId = teacher.getId();

        if (studentId == null) {
            throw new DataNotFoundException("학생 정보가 입력되지 않았습니다.");
        }

        RecordSaveReqDto recordSaveReqDto = new RecordSaveReqDto();
        recordSaveReqDto.setStudentId(studentId);
        recordSaveReqDto.setTeacherId(teacherId);

        model.addAttribute("recordSaveReqDto", recordSaveReqDto);

        return "record/record_create_form";
    }

    @PostMapping("/record/create")
    public String createRecord(@Validated @ModelAttribute RecordSaveReqDto recordSaveReqDto, BindingResult bindingResult, HttpServletRequest request, Model model) {
        HttpSession session = request.getSession(false);
        Object loginSession = session.getAttribute(SessionConst.LOGIN_MEMBER_SESSION);

        if (session == null || loginSession == null) {
            return "redirect:/login";
        }

        // 선생님으로 로그인한 것이 아니라면 홈페이지로 redirect
        if (!(loginSession instanceof Teacher)) {
            return "redirect:/";
        }

        if (bindingResult.hasErrors()) {
            log.info("error={}", bindingResult);
            model.addAttribute("recordSaveReqDto", recordSaveReqDto);
            return "record/record_create_form";
        }

        recordService.saveRecord(recordSaveReqDto);
        return "redirect:/record/" + recordSaveReqDto.getStudentId();
    }

    @GetMapping("/record/update/{recordId}")
    public String showUpdateRecordForm(@PathVariable("recordId") Long recordId, @RequestParam("studentId") Long studentId, HttpServletRequest request, Model model) {
        HttpSession session = request.getSession(false);
        Object loginSession = session.getAttribute(SessionConst.LOGIN_MEMBER_SESSION);

        if (session == null || loginSession == null) {
            return "redirect:/login";
        }

        // 선생님으로 로그인한 것이 아니라면 홈페이지로 redirect
        if (!(loginSession instanceof Teacher)) {
            return "redirect:/";
        }

        RecordRespDto recordRespDto = recordService.getRecord(recordId);

        RecordUpdateReqDto recordUpdateReqDto = new RecordUpdateReqDto();
        recordUpdateReqDto.setRecordId(recordId);
        recordUpdateReqDto.setStudentId(studentId);
        recordUpdateReqDto.setContent(recordRespDto.getContent());

        model.addAttribute("recordUpdateReqDto", recordUpdateReqDto);

        return "record/record_update_form";
    }

    @PostMapping("/record/update/{recordId}")
    public String updateRecord(@Validated @ModelAttribute RecordUpdateReqDto recordUpdateReqDto, BindingResult bindingResult, HttpServletRequest request, Model model) {

        HttpSession session = request.getSession(false);
        Object loginSession = session.getAttribute(SessionConst.LOGIN_MEMBER_SESSION);

        if (session == null || loginSession == null) {
            return "redirect:/login";
        }

        // 선생님으로 로그인한 것이 아니라면 홈페이지로 redirect
        if (!(loginSession instanceof Teacher)) {
            return "redirect:/";
        }

        if (bindingResult.hasErrors()) {
            model.addAttribute("recordUpdateReqDto", recordUpdateReqDto);
            return "record/record_update_form";
        }

        recordService.modifyContent(recordUpdateReqDto.getRecordId(), recordUpdateReqDto.getContent());

        return "redirect:/record/" + recordUpdateReqDto.getStudentId();
    }

    @GetMapping("/record/delete/{recordId}")
    public String deleteRecord(@PathVariable("recordId") Long recordId, @RequestParam("studentId") Long studentId) {
        recordService.deleteRecord(recordId);

        return "redirect:/record/" + studentId;
    }
}
