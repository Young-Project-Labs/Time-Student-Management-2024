package com.time.studentmanage.domain.dto.student;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class FindIdDto {
    @NotBlank
    private String name;
    @NotBlank
    private String email;
}
