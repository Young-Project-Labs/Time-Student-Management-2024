package com.time.studentmanage.web;

import com.time.studentmanage.domain.dto.record.RecordRespDTO;
import com.time.studentmanage.domain.dto.record.RecordSaveReqDTO;
import com.time.studentmanage.domain.dto.record.RecordSearchDTO;
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

import java.util.ArrayList;
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
    public String records(@PathVariable("studentId") Long id,
                          HttpServletRequest request, Model model) {
        HttpSession session = request.getSession(false);

        if (session == null) {
            // TODO: 로그인 페이지로 이동하도록 변경
            return "redirect:/";
        }

        StudentRespDto studentRespDto = studentService.getStudentInfo(id);
        List<RecordRespDTO> recordList = recordService.getStudentList(id);

        RecordSearchDTO recordSearchDTO = new RecordSearchDTO();
        recordSearchDTO.setStudentName(studentRespDto.getName());

        model.addAttribute("recordSearchDTO", recordSearchDTO);
        model.addAttribute("recordList", recordList);

        return "record/record_list";
    }

    @PostMapping("/record/{studentId}")
    public String filterRecords(@Validated @ModelAttribute RecordSearchDTO recordSearchDTO, BindingResult result,
                                @PathVariable("studentId") Long studentId,
                                HttpServletRequest request,
                                Model model) {

        HttpSession session = request.getSession(false);
        Object teacherSession = session.getAttribute(SessionConst.LOGIN_MEMBER_SESSION);

        if (session == null || teacherSession == null) {
            // TODO: 로그인 페이지로 이동하도록 변경
            return "redirect:/";
        }

        if (result.hasErrors()) {
            log.info("errors={}", result);
            List<RecordRespDTO> recordList = recordService.getStudentList(studentId);
            model.addAttribute("recordList", recordList);
            model.addAttribute("recordSearchDTO", recordSearchDTO);
            return "record/record_list";
        }

        Teacher teacher = (Teacher) teacherSession;
        recordSearchDTO.setTeacherId(teacher.getId());

        StudentRespDto studentRespDto = studentService.getStudentInfo(studentId);
        List<RecordRespDTO> recordList = recordService.getFilteredResults(recordSearchDTO);

        model.addAttribute("recordList", recordList);
        model.addAttribute("studentName", studentRespDto.getName());
        model.addAttribute("recordSearchDTO", recordSearchDTO);

        return "record/record_list";
    }

    @GetMapping("/record/create")
    public String showCreateRecordForm(@RequestParam(value = "studentId", required = false) Long studentId,
                                       Model model,
                                       HttpServletRequest request) {
        HttpSession session = request.getSession(false);

        if (session == null) {
            // TODO: 로그인 페이지로 이동하도록 변경
            return "redirect:/";
        }

        if (studentId == null) {
            throw new DataNotFoundException("학생 정보가 입력되지 않았습니다.");
        }

        RecordSaveReqDTO recordSaveReqDTO = new RecordSaveReqDTO();
        recordSaveReqDTO.setStudentId(studentId);
        recordSaveReqDTO.setTeacherId(3L);

        model.addAttribute("recordSaveReqDTO", recordSaveReqDTO);

        return "record/record_create_form";
    }

    @PostMapping("/record/create")
    public String createRecord(@Validated @ModelAttribute RecordSaveReqDTO recordSaveReqDTO, BindingResult bindingResult,
                               HttpServletRequest request, Model model) {

        HttpSession session = request.getSession(false);

        if (session == null) {
            // TODO: 로그인 페이지로 이동하도록 변경
            return "redirect:/";
        }

        if (bindingResult.hasErrors()) {
            log.info("error={}", bindingResult);
            model.addAttribute("recordSaveReqDTO", recordSaveReqDTO);
            return "record/record_create_form";
        }

        recordService.saveRecord(recordSaveReqDTO);
        return "redirect:/record/" + recordSaveReqDTO.getStudentId();
    }

    @GetMapping("/record/update/{recordId}")
    public String showUpdateRecordForm(@PathVariable("recordId") Long recordId,
                                       @RequestParam("studentId") Long studentId,
                                       HttpServletRequest request,
                                       Model model) {
        HttpSession session = request.getSession(false);

        if (session == null) {
            // TODO: 로그인 페이지로 이동하도록 변경
            return "redirect:/";
        }

        RecordRespDTO recordRespDTO = recordService.getRecord(recordId);

        model.addAttribute("studentId", studentId);
        model.addAttribute("record", recordRespDTO);
        return "record/record_update_form";
    }

    @PostMapping("/record/update/{recordId}")
    public String updateRecord(@PathVariable("recordId") Long recordId,
                               @RequestParam("content") String content,
                               @RequestParam("studentId") Long studentId) {
        recordService.modifyContent(recordId, content);
        return "redirect:/record/" + studentId;
    }

    @GetMapping("/record/delete/{recordId}")
    public String deleteRecord(@PathVariable("recordId") Long recordId,
                               @RequestParam("studentId") Long studentId) {
        recordService.deleteRecord(recordId);

        return "redirect:/record/" + studentId;
    }
}
