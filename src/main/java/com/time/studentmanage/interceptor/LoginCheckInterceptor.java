package com.time.studentmanage.interceptor;

import com.time.studentmanage.config.Auth;
import com.time.studentmanage.domain.enums.Position;
import com.time.studentmanage.domain.member.Teacher;
import com.time.studentmanage.web.login.SessionConst;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import org.springframework.security.access.AccessDeniedException;
import java.util.Arrays;

@Slf4j
public class LoginCheckInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String requestURI = request.getRequestURI();
        log.info("로그인 체크 ={}", requestURI);

        HttpSession session = request.getSession(false);
        //세션이 없을 때 처리
        if (session.getAttribute(SessionConst.LOGIN_MEMBER_SESSION) == null) {
            log.info("세션이 없습니다.");
            //세션이 없는 경우 로그인 페이지로 이동합니다.
            response.sendRedirect("/login");
            return false;
        }

        // HandlerMethod 클래스가 아닌 경우 정상 요청 (컨트롤러 요청이 아닌 요청들은 true)
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }

        HandlerMethod handlerMethod = (HandlerMethod) handler;

        Auth auth = handlerMethod.getMethodAnnotation(Auth.class);
        //auth == null -> 권한 검증 X
        if (auth == null) {
            return true;

        }else{
        // auth != null -> 권한 검증
            //1. 세션 저장 member 조회
            Object loginMember = session.getAttribute(SessionConst.LOGIN_MEMBER_SESSION);

            if (loginMember instanceof Teacher){
                Teacher loginTeacher = (Teacher) loginMember;
                Position loginTeacherPosition = loginTeacher.getPosition();

                //2. 권한 검증
                if ((Arrays.stream(auth.role()).anyMatch(role -> role.name().equals(loginTeacherPosition.toString())))) { // 같은 String 타입으로 비교
                    return true;
                } else {
                    throw new AccessDeniedException("접근할 수 없습니다.");
                }
            }else{
                //학생은 권한 검증 X
                throw new AccessDeniedException("접근할 수 없습니다.");
            }
        }
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
    }
}
