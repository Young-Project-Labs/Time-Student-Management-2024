package com.time.studentmanage.config;

import com.time.studentmanage.interceptor.LogInterceptor;
import com.time.studentmanage.interceptor.LoginCheckInterceptor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Component
public class WebConfig implements WebMvcConfigurer {

    /**
     *
     * @param registry
     * 인터셉터들을 등록한다.
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LogInterceptor())
                .order(1)
                .addPathPatterns("/**")
                .excludePathPatterns(
                        "/css/**", "/error", "/error-page/**");
        // 세션 검증, 권한 검증 인터셉터
        registry.addInterceptor(new LoginCheckInterceptor())
                .order(2)
                .addPathPatterns("/student/**", "/school", "/search", "/teacher/**", "/record/**")
                .excludePathPatterns("/css/**", "/error", "/error-page/**", "/login","/logout", "/join", "/student/findid", "/student/findpwd");
    }
}
