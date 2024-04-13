package com.time.studentmanage.web.login;

import com.time.studentmanage.domain.enums.MemberType;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString
public class LoginFormDto {
    @NotBlank
    private String loginId;
    @NotBlank
    private String password;
}
