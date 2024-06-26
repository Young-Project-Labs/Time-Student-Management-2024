package com.time.studentmanage.web.record;

import com.time.studentmanage.config.Auth;
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

    private static RecordUpdateReqDto createRecordUpdateReqDto(Long studentId, RecordRespDto recordRespDto) {
        RecordUpdateReqDto recordUpdateReqDto = new RecordUpdateReqDto();
        recordUpdateReqDto.setRecordId(recordRespDto.getRecordId());
        recordUpdateReqDto.setStudentId(studentId);
        recordUpdateReqDto.setTitle(recordRespDto.getTitle());
        recordUpdateReqDto.setContent(recordRespDto.getContent());
        return recordUpdateReqDto;
    }

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
                          Model model) {

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
        Object loginSession = request.getSession().getAttribute(SessionConst.LOGIN_MEMBER_SESSION);

        if (loginSession instanceof Teacher teacher) {
            RecordRespDto record = recordService.getRecord(recordId, teacher);
            model.addAttribute("record", record);
        }
        if (loginSession instanceof Student student) {
            RecordRespDto record = recordService.getRecord(recordId, student);
            model.addAttribute("record", record);
        }


        model.addAttribute("studentId", studentId);
        return "record/record_detail";
    }

    @Auth(role = {Auth.Role.TEACHER, Auth.Role.ADMIN, Auth.Role.CHIEF})
    @GetMapping("/record/create")
    public String showCreateRecordForm(@RequestParam(value = "studentId", required = false) Long studentId,
                                       Model model, HttpServletRequest request) {

        Teacher teacher = (Teacher) request.getSession(false).getAttribute(SessionConst.LOGIN_MEMBER_SESSION);

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

    @Auth(role = {Auth.Role.TEACHER, Auth.Role.ADMIN, Auth.Role.CHIEF})
    @PostMapping("/record/create")
    public String createRecord(@Validated @ModelAttribute RecordSaveReqDto recordSaveReqDto, BindingResult bindingResult, Model model) {

        if (bindingResult.hasErrors()) {
            log.info("error={}", bindingResult);
            model.addAttribute("recordSaveReqDto", recordSaveReqDto);
            return "record/record_create_form";
        }

        recordService.saveRecord(recordSaveReqDto);
        return "redirect:/record/" + recordSaveReqDto.getStudentId();
    }

    @Auth(role = {Auth.Role.TEACHER, Auth.Role.ADMIN, Auth.Role.CHIEF})
    @GetMapping("/record/update/{recordId}")
    public String showUpdateRecordForm(@PathVariable("recordId") Long recordId, @RequestParam("studentId") Long studentId, HttpServletRequest request, Model model) {
        Teacher teacher = (Teacher) request.getSession(false).getAttribute(SessionConst.LOGIN_MEMBER_SESSION);

        RecordRespDto recordRespDto = recordService.getRecord(recordId, teacher);
        RecordUpdateReqDto recordUpdateReqDto = createRecordUpdateReqDto(studentId, recordRespDto);
        model.addAttribute("recordUpdateReqDto", recordUpdateReqDto);

        return "record/record_update_form";
    }

    @Auth(role = {Auth.Role.TEACHER, Auth.Role.ADMIN, Auth.Role.CHIEF})
    @PostMapping("/record/update/{recordId}")
    public String updateRecord(@Validated @ModelAttribute RecordUpdateReqDto recordUpdateReqDto,
                               BindingResult bindingResult,
                               Model model) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("recordUpdateReqDto", recordUpdateReqDto);
            return "record/record_update_form";
        }

        recordService.modifyContent(recordUpdateReqDto.getRecordId(), recordUpdateReqDto.getTitle(), recordUpdateReqDto.getContent());

        return "redirect:/record/" + recordUpdateReqDto.getStudentId();
    }

    @Auth(role = {Auth.Role.TEACHER, Auth.Role.ADMIN, Auth.Role.CHIEF})
    @GetMapping("/record/delete/{recordId}")
    public String deleteRecord(@PathVariable("recordId") Long recordId, @RequestParam("studentId") Long studentId) {
        recordService.deleteRecord(recordId);

        return "redirect:/record/" + studentId;
    }
}
