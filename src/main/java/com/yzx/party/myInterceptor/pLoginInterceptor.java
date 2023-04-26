package com.yzx.party.myInterceptor;

import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author yzx
 * @version JDK 1.8.0_131
 * @since 2022/1/15 16:56
 */
//拦截器 用于拦截没有登录直接访问前台
public class pLoginInterceptor implements HandlerInterceptor {
    //return true 就是不拦截
    //return false 就是拦截
    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) throws Exception {
        //登录成功之后获取用户的信息 有session
        Object loginUser = request.getSession().getAttribute("loginPerson");

        //拦截
        if (loginUser == null){
            //重定向回去
            response.sendRedirect("/personShow");
            return false;
        }else {
            return true;
        }
    }
}
