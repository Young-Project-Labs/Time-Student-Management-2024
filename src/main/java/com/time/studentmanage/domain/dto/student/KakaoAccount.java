package com.time.studentmanage.domain.dto.student;

import com.time.studentmanage.domain.enums.GenderType;
import lombok.*;

import java.util.HashMap;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class KakaoAccount {
    private String email;
//    private String name;
//    private GenderType gender;
//    private String phoneNumber;

    public KakaoAccount(HashMap<String, Object> kakaoUserInfo) {
        this.email = kakaoUserInfo.get("email").toString();
//        this.name = kakaoUserInfo.get("name").toString();
//        this.gender = GenderType.valueOf(kakaoUserInfo.get("gender").toString().toUpperCase());
//        this.phoneNumber = kakaoUserInfo.get("phone_number").toString();
    }
}
