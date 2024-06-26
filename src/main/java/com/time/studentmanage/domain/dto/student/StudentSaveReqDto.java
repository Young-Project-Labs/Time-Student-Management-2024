package com.time.studentmanage.domain.dto.student;

import com.time.studentmanage.domain.enums.*;
import com.time.studentmanage.domain.member.Address;
import com.time.studentmanage.domain.member.Student;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class StudentSaveReqDto {
    @NotBlank
    private String name;
    @NotBlank
    private String userId;
    @NotBlank
    private String password;
    @NotBlank
    @Pattern(regexp = "^(010|011|016|017|018|019)-\\d{3,4}-\\d{4}$")
    private String phoneNumber;
    @NotBlank
    private String schoolName;
    @NotBlank
    private String parentName;
    @NotBlank
    @Pattern(regexp = "^(010|011|016|017|018|019)-\\d{3,4}-\\d{4}$")
    private String parentPhoneNumber;
    @NotBlank
    @Pattern(regexp = "^[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*.[a-zA-Z]{2,3}$") // 이메일 정규표현식
    private String email;
    @NotNull
    @Min(1)
    @Max(6)
    private Integer grade;

    private AttendanceStatus attendanceStatus;

    private MemberType memberType;

    @NotNull
    private GenderType gender;
    @NotNull
    private ClassType classType;
    @Valid
    private Address address;


    //Dto -> Student 엔티티 변환
    public Student toEntity(BCryptPasswordEncoder bCryptPasswordEncoder) {
        Student student = Student.builder()
                .name(name)
                .userId(userId)
                .password(bCryptPasswordEncoder.encode(password))
                .phoneNumber(phoneNumber)
                .schoolName(schoolName)
                .grade(grade)
                .attendanceStatus(attendanceStatus)
                .memberType(memberType)
                .gender(gender)
                .provider(ProviderType.GENERAL)
                .classType(classType)
                .parentName(parentName)
                .parentPhoneNumber(parentPhoneNumber)
                .address(address)
                .build();
        return student;
    }
}
