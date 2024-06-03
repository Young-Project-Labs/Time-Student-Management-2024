package com.time.studentmanage.domain.dto.classroom;

import com.time.studentmanage.domain.enums.SearchType;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ClassRoomSearchReqDto {
    private SearchType searchType;
    private String content;
    private Long teacherId;
    @Min(0)
    private int page;
}
