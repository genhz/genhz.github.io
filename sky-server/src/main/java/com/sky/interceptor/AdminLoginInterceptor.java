package com.sky.interceptor;

import com.sky.context.UserHolder;
import com.sky.properties.JwtProperties;
import com.sky.utils.JwtUtil;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * jwt令牌登录校验的拦截器
 */
@Component
@Slf4j
public class AdminLoginInterceptor implements HandlerInterceptor {

    @Autowired
    private JwtProperties jwtProperties;

	// 校验jwt
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        log.info("拦截资源路径: {}",request.getRequestURL());

        //1、从请求头中获取令牌
        String token = request.getHeader("token");

        //2、校验令牌
        try {
            Claims claims = JwtUtil.parseJWT(jwtProperties.getAdminSecret(), token);
            Long empId = Long.valueOf(claims.get("empId").toString());

            //将员工id放入ThreadLocal
            UserHolder.set(empId);

            //3、通过，放行
            return true;
        } catch (Exception ex) {
            //4、不通过，响应401状态码
            response.setStatus(401);
            return false;
        }
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        //将员工id从ThreadLocal移除
        UserHolder.remove();
    }
}
