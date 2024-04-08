package com.time.studentmanage.domain.dto.teacher;

import com.time.studentmanage.domain.enums.GenderType;
import com.time.studentmanage.domain.enums.MemberType;
import com.time.studentmanage.domain.enums.Position;
import com.time.studentmanage.domain.member.Teacher;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.bind.DefaultValue;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class TeacherSaveReqDto {
    @NotBlank
    private String name;
    @NotBlank
    private String phoneNumber;
    @NotBlank
    @Pattern(regexp = "^[\\w.-]+@time\\.com$")
    private String email;
    @NotBlank
    private String password;
    @NotNull
    private Position position;
    private MemberType memberType;
    @NotNull
    private GenderType gender;
    

    // 엔티티 변환
    public Teacher toEntity(BCryptPasswordEncoder bCryptPasswordEncoder) {
        return Teacher.builder()
                .name(name)
                .phoneNumber(phoneNumber)
                .email(email)
                .password(bCryptPasswordEncoder.encode(password))
                .position(position)
                .memberType(memberType)
                .gender(gender)
                .build();
    }
}
