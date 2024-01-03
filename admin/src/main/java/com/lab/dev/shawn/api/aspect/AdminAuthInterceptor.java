package com.lab.dev.shawn.api.aspect;

import com.lab.dev.shawn.api.base.constant.BaseExceptionEnum;
import com.lab.dev.shawn.api.base.exception.BaseException;
import com.lab.dev.shawn.api.util.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

public class AdminAuthInterceptor implements HandlerInterceptor {
    public static final String TOKEN_KEY = "TOKEN";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (request.getMethod().equalsIgnoreCase("OPTIONS"))
            return true;

        String token = request.getHeader("X-Token");
        isTokenValid(token);

        return true;
    }

    private static void isTokenValid(String token) throws BaseException {
        try{
            if (token == null || JwtUtil.getTokenClaims(token) == null) {
                throw new BaseException(BaseExceptionEnum.NOT_LOGIN);
            }
        }catch (Exception e){
            throw new BaseException(BaseExceptionEnum.NOT_LOGIN);
        }
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
//        // 在请求处理完成后的操作
//        // 获取Token，可以从Session或其他地方获取

//        response.
//        if (token != null) {
//            // 将Token添加到请求的Header中
//            response.addHeader(TOKEN_KEY, token);
//        }
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        // 在请求完成后的操作
    }
}
