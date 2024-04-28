package com.time.studentmanage.web.record;

import com.time.studentmanage.service.RecordService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class RecordApiController {
    private final RecordService recordService;

//    @GetMapping("/record/list/{id}")
//    public ResponseEntity<?> showRecords(@PathVariable("id") Long id,
//                                         @Validated @ModelAttribute RecordSearchDto recordSearchDto, BindingResult bindingResult,
//                                         @RequestParam(value = "page", defaultValue = "0") int page) {
//
//        // 검증 오류 처리
//        if (bindingResult.hasErrors()) {
//            // 오류 응답 생성
//            List<BindingErrorResponse> errors = new ArrayList<>();
//            bindingResult.getFieldErrors().forEach(fieldError ->
//                    errors.add(new BindingErrorResponse(fieldError.getField(), fieldError.getDefaultMessage())));
//
//            return ResponseEntity.badRequest().body(new PagingRespResult(null, errors));
//        }
//
//        recordSearchDto.setStudentId(id);
//        Page<RecordRespDto> pagingResult = recordService.getFilteredResults(recordSearchDto, page);
//
//        return ResponseEntity.ok(new PagingRespResult(pagingResult, null));
//    }

    @Data
    @AllArgsConstructor
    static class BindingErrorResponse {
        private String fieldName;
        private String message;
    }

    @Data
    @AllArgsConstructor
    static class PagingRespResult<T> {
        private T data; // 성공 결과를 담을 필드
        private T errors; // 실패 시 검증 오류 결과를 담을 필드
    }

}
