package com.time.studentmanage.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.time.studentmanage.exception.OAuthException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
public class OAuthService {
    /**
     * 카카오 로그인 엑세스 토큰 요청(POST)
     * code -> 카카오 서버에서 accessToken를 받아오는 요청.
     */
    public String getKakaoAccessToken(String client_id, String redirect_uri, String code) {
        try {
            //POST 요청으로 accessToken 받기
            String reqURL = "https://kauth.kakao.com/oauth/token?grant_type=authorization_code&redirect_uri=" + redirect_uri + "&client_id=" + client_id + "&code=" + code;

            URL url = new URL(reqURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");

            //POST 요청 후 받아오는 로직
            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));

            String line = "";
            String body = "";

            while ((line = br.readLine()) != null) {
                body += line;
            }

            ObjectMapper objectMapper = new ObjectMapper();
            Map<String, Object> jsonMap = objectMapper.readValue(body, new TypeReference<Map<String, Object>>() {
            });

            log.info("Response Body :" + body);

            String accessToken = (String) jsonMap.get("access_token");
            String refreshToken = (String) jsonMap.get("refresh_token");
            String scope = (String) jsonMap.get("scope");

            log.info("accessToken={}", accessToken);
            log.info("refreshToken={}", refreshToken);
            log.info("scope={}", scope);

            return accessToken;
        } catch (IOException e) {
            throw new OAuthException("토큰을 받아 오는 중 예외 발생");
        }
    }


    /**
     * 사용자 정보 추출
     */

    public HashMap<String, Object> getKakaoUserInfo(String access_token) throws IOException {
        try {
            HashMap<String, Object> userInfo = new HashMap<>();

            // kakao GET 요청(access_token으로 카카오서버에 사용자 정보를 가져오는 요청)
            String reqURL = "https://kapi.kakao.com/v2/user/me";
            URL url = new URL(reqURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            //필수인 access_token을 저장.
            conn.setRequestProperty("Authorization", "Bearer " + access_token);

            //응답 코드
            int responseCode = conn.getResponseCode();
            log.info("responseCode={}", responseCode);

            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));

            String line = "";
            String body = "";

            while ((line = br.readLine()) != null) {
                body += line;
            }

            log.info("response Body={}", body);

            ObjectMapper objectMapper = new ObjectMapper();
            Map<String, Object> jsonMap = objectMapper.readValue(body, new TypeReference<Map<String, Object>>() {
            });

            //사용자 정보 추출
            Map<String, Object> properties = (Map<String, Object>) jsonMap.get("properties");
            Map<String, Object> kakao_account = (Map<String, Object>) jsonMap.get("kakao_account");

//            TODO: 테스트에서는 name, gender, phone_number를 받아올 수 있지만 실제로 배포하면 email만 받아올 수 있기에 dto 및 해당 부분 로직 수정 필요.
            Long id = (Long) jsonMap.get("id");
            String email = kakao_account.get("email").toString();
//            String name = kakao_account.get("name").toString();
//            String gender = kakao_account.get("gender").toString();
//            String phone_number = kakao_account.get("phone_number").toString();

            //추출 후 유저정보 map에 put
            userInfo.put("email", email);
//            userInfo.put("name", name);
//            userInfo.put("gender", gender);
//            userInfo.put("phone_number", phone_number);

            return userInfo;
        } catch (IOException e) {
            throw new OAuthException("사용자 정보를 받아 오는 중 예외 발생");
        }
    }
}
