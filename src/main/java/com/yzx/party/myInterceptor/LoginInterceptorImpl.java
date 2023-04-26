package com.yzx.party.myInterceptor;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author yzx
 * @version JDK 1.8.0_131
 * @since 2022/1/6 11:11
 */
//拦截器的控制类 就是拦截哪些 不拦截哪些
@Configuration
public class LoginInterceptorImpl implements WebMvcConfigurer {
    //重写拦截器请求
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //分别是 添加新的拦截器 拦截哪些请求 那些请求不拦截
        registry.addInterceptor(new LoginInterceptor())
                //过滤admin下面的所有 即学校管理员和学院管理员的访问信息都会被禁止
                .addPathPatterns("/admin/**")
                //但是不过滤登录页面和登录提交页面，不拦截找回密码页面和找回密码的提交页面
                .excludePathPatterns("/admin","/admin/login","/admin/toFindBack","/admin/findBack/**","/admin/forgetCode","/admin/toLoginPage");
    }
}
