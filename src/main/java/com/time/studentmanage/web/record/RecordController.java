package com.time.studentmanage.web.record;

import com.time.studentmanage.domain.dto.record.RecordRespDto;
import com.time.studentmanage.domain.dto.record.RecordSaveReqDto;
import com.time.studentmanage.domain.dto.record.RecordSearchDto;
import com.time.studentmanage.domain.dto.record.RecordUpdateReqDto;
import com.time.studentmanage.domain.dto.student.StudentRespDto;
import com.time.studentmanage.domain.enums.SearchType;
import com.time.studentmanage.domain.member.Student;
import com.time.studentmanage.domain.member.Teacher;
import com.time.studentmanage.exception.DataNotFoundException;
import com.time.studentmanage.service.RecordService;
import com.time.studentmanage.service.StudentService;
import com.time.studentmanage.web.login.SessionConst;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.stream.Collectors;

@Slf4j
@Controller
@RequiredArgsConstructor
public class RecordController {

    private final RecordService recordService;
    private final StudentService studentService;

    @ModelAttribute("searchTypeOptions")
    public SearchType[] searchType() {
        SearchType[] filteredSearchTypes = Arrays.stream(SearchType.values())
                .filter(type -> type == SearchType.CONTENT ||
                        type == SearchType.TEACHER_NAME)
                .collect(Collectors.toList())
                .toArray(new SearchType[0]); // 배열 타입을 알려주기 위함

        return filteredSearchTypes;
    }

    @GetMapping("/record/{studentId}")
    public String records(@PathVariable("studentId") Long id,
                          @ModelAttribute("recordSearchDto") RecordSearchDto recordSearchDto,
                          @RequestParam(value = "page", defaultValue = "0") int page,
                          HttpServletRequest request, Model model) {
        HttpSession session = request.getSession(false);
        Object loginSession = session.getAttribute(SessionConst.LOGIN_MEMBER_SESSION);

        if (session == null || loginSession == null) {
            return "redirect:/login";
        }
        StudentRespDto studentInfo = studentService.getStudentInfo(id);
        String studentName = studentInfo.getName();
        Page<RecordRespDto> pagingResult = recordService.getAllStudentRecord(id, page);

        model.addAttribute("pagingResult", pagingResult);
        model.addAttribute("studentName", studentName);
        return "record/record_list";
    }

    /**
     * 페이지네이션 업데이트 처리 및 검색 타입에 따른 검색 결과 처리
     */
    @GetMapping("/record/list/{studentId}")
    public String showSearchedRecordResult(@PathVariable("studentId") Long studentId, // recordSearchDto의 studentId와 필드명이 같으면 스프링에서 자동으로 바인딩 해줌
                                           @ModelAttribute("recordSearchDto") RecordSearchDto recordSearchDto,
                                           Model model) {

        Page<RecordRespDto> pagingResult = recordService.getPaginationResultWithSearchCondition(recordSearchDto, studentId);
        StudentRespDto studentInfo = studentService.getStudentInfo(studentId);
        String studentName = studentInfo.getName();

        model.addAttribute("pagingResult", pagingResult);
        model.addAttribute("studentName", studentName);

        return "record/record_list";
    }

    @GetMapping("/record/detail/{id}")
    public String showRecordDetail(@PathVariable("id") Long recordId,
                                   @RequestParam("studentId") Long studentId,
                                   Model model,
                                   HttpServletRequest request) {
        Object validUser = request.getSession().getAttribute(SessionConst.LOGIN_MEMBER_SESSION);

        RecordRespDto record = recordService.getRecord(recordId, validUser);

        model.addAttribute("studentId", studentId);
        model.addAttribute("record", record);
        return "record/record_detail";
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
        if (!(loginSession instanceof Teacher teacher)) {
            return "redirect:/";
        }

        RecordRespDto recordRespDto = recordService.getRecord(recordId, teacher.getId());

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
