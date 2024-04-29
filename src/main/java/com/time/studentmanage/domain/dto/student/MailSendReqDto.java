package com.time.studentmanage.domain.dto.student;

import com.time.studentmanage.domain.enums.MailSearchType;
import com.time.studentmanage.web.login.FindIdValidation;
import com.time.studentmanage.web.login.FindPwdValidation;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class MailSendReqDto {
    @NotNull
    private MailSearchType searchType; //userID, pwd

    @NotBlank(groups = FindIdValidation.class)
    private String name;

    @NotBlank(groups = FindPwdValidation.class)
    private String userId;

    @NotBlank(groups = {FindIdValidation.class, FindPwdValidation.class})
    @Pattern(regexp = "^[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*.[a-zA-Z]{2,3}$") // 이메일 정규표현식
    private String email;
}


