package com.time.studentmanage.domain.dto.record;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class RecordUpdateReqDto {
    @NotNull
    Long recordId;
    @NotNull
    Long studentId;
    @NotBlank
    String title;
    @NotBlank
    String content;
}
