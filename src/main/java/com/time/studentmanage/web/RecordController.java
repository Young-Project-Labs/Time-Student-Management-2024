package com.time.studentmanage.web;

import com.time.studentmanage.domain.dto.record.RecordRespDTO;
import com.time.studentmanage.domain.dto.record.RecordSaveReqDTO;
import com.time.studentmanage.domain.member.Student;
import com.time.studentmanage.service.RecordService;
import com.time.studentmanage.service.StudentService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
public class RecordController {

    private final RecordService recordService;
    private final StudentService studentService;

    @GetMapping("/record/{studentId}")
    public String records(Model model, @PathVariable("studentId") Long id) {
        Student student = studentService.getStudentInfo(id);
        List<RecordRespDTO> recordList = recordService.getStudentList(id);

        if (!recordList.isEmpty()) {
            model.addAttribute("recordList", recordList);
        }
        model.addAttribute("studentName", student.getName());

        return "record/record_list";
    }

    @GetMapping("/record/create")
    public String showCreateRecordForm(@RequestParam("studentId") Long id, Model model) {
        model.addAttribute("studentId", id);
        return "record/record_create_form";
    }

    @PostMapping("/record/create")
    public String createRecord(@RequestParam("studentId") Long studentId,
//                               @RequestParam("teacherId") Long teacherId,
                               @RequestParam("content") String content,
                               HttpServletRequest request) {
        // TODO: 세션을 이용해서 선생님 정보를 가져오도록 로직 변경해야 함.
//        HttpSession session = request.getSession(true);
//        Long teacherId = (Long) session.getAttribute("teacherId");
        Long teacherId = 3L;

        RecordSaveReqDTO recordSaveReqDTO = RecordSaveReqDTO.builder()
                .studentId(studentId)
                .teacherId(teacherId)
                .content(content).build();

        recordService.saveRecord(recordSaveReqDTO);
        return "redirect:/record/" + studentId;
    }

    @GetMapping("/record/update/{recordId}")
    public String showUpdateRecordForm(@PathVariable("recordId") Long recordId,
                                       @RequestParam("studentId") Long studentId,
                                       Model model) {
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
