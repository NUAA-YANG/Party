package com.yzx.party.myException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

/**
 * @author yzx
 * @version JDK 1.8.0_131
 * @since 2022/1/5 9:19
 */
/*拦截错误,并且跳转到我们自定义的错误页面*/
public class ExceptionRedirect {

    //拿到日志错误对象
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @ExceptionHandler(Exception.class)
    public ModelAndView exceptionHandler(HttpServletRequest request, Exception e) throws Exception {
        //获得错误的记录
        logger.error("Request URl:{} , Exception : {}",request.getRequestURL(),e);

        //并且不是拦截所有的异常
        // ResponseStatus对应的是我们是定义的NotFoundException中的注释
        // 如果是运行时异常，直接在控制台抛出异常，交给springboot本身自带的异常处理
        if (AnnotationUtils.findAnnotation(e.getClass(), ResponseStatus.class) != null){
            throw e;
        }
        //如果是我们自己的错误，那么跳转到我们自定义的异常处理页面
        //返回对象 把内容封装进去
        ModelAndView mv = new ModelAndView();
        mv.addObject("url",request.getRequestURL());
        mv.addObject("exception",e);
        //设置返回的页面 也是从templates下面开始计算 我们自定义的页面
        mv.setViewName("error/error");
        return mv;
    }
}
