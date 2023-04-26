package com.yzx.party.myInterceptor;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author yzx
 * @version JDK 1.8.0_131
 * @since 2022/1/6 11:07
 */
//拦截器 用于拦截没有登录直接访问后台
public class LoginInterceptor implements HandlerInterceptor {
    //return true 就是不拦截
    //return false 就是拦截
    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) throws Exception {
        //登录成功之后获取用户的信息 有session
        Object loginUser = request.getSession().getAttribute("admin");

        //拦截
        if (loginUser == null){
            //重定向回去
            response.sendRedirect("/admin ");
            return false;
        }else {
            return true;
        }
    }
}
