package com.time.studentmanage.domain.dto.student;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class StudentUpdatePwdReqDto {
    @NotNull
    private Long studentId;
    @NotBlank(message = "패스워드를 입력해주세요.")
    private String password;

}
