package com.time.studentmanage.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import java.util.UUID;

@Slf4j
public class LogInterceptor implements HandlerInterceptor {

    public static final String LOG_ID = "logId";

    /**
     * 컨트롤러 호출 전 적절한 접근인지 확인하고 처리할 컨트롤러를 호출해줌
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String requestURI = request.getRequestURI();

        String uuid = UUID.randomUUID().toString();
        request.setAttribute(LOG_ID, uuid);

        log.info("REQUEST=[{}]\nDispatcherType=[{}]\nHandler[{}]", requestURI, request.getDispatcherType(), handler);
        return true;
    }

    /**
     * 컨트롤러(핸들러)가 반환하는 modelAndView를 넘겨줌
     */
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        log.info("Called PostHandler=[{}]", modelAndView);
    }


    /**
     * 정상 흐름에서는 ex가 null
     * 뷰가 렌더링 된 이후에 호출된다.
     * 예외가 나도 항상 요청 마지막에 호출된다. finally 구문과 같다.
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        String requestURI = request.getRequestURI();
        String logId = (String) request.getAttribute(LOG_ID);

        log.info("RESPONSE=[{}]\nDispatcherType=[{}]\nHandler[{}]", requestURI, request.getDispatcherType(), requestURI);

        if (ex != null) {
            log.error("ERROR OCCURRED", ex);
        }

    }
}
