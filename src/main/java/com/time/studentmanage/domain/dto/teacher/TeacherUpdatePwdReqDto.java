package com.time.studentmanage.domain.dto.teacher;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TeacherUpdatePwdReqDto {
    @NotNull
    private Long teacherId;
    @NotBlank(message = "패스워드를 입력해주세요.")
    private String password;
}
