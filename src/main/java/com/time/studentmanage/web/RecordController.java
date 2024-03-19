package com.time.studentmanage.web;

import com.time.studentmanage.domain.dto.record.RecordRespDTO;
import com.time.studentmanage.service.RecordService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
public class RecordController {

    private final RecordService recordService;

    @GetMapping("/record/{studentId}")
    public String records(Model model, @PathVariable("studentId") Long id) {
        List<RecordRespDTO> recordList = recordService.getStudentList(id);
        String studentName = recordList.get(0).getStudentName();

        model.addAttribute("studentName", studentName);
        model.addAttribute("recordList", recordList);

        return "record/record_list";
    }

    @GetMapping("/record/create")
    public String createRecord() {
        return "record/record_create_form";
    }
}
