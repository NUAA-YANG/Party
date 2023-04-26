package com.yzx.party.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;

/**
 * @author yzx
 * @version JDK 1.8.0_131
 * @since 2022/1/5 9:14
 */
//日志处理模块
@Aspect
@Component
public class LogAspect {

    //获取当前日志
    private final Logger logger =  LoggerFactory.getLogger(this.getClass());

    //所有在这个包下面的请求都会切面
    @Pointcut("execution(* com.yzx.party.controller.*.*(..) )")
    public void log(){ }

    @Before("log()")
    //JoinPoint获得日志对象
    public void doBefore(JoinPoint joinPoint){
        //通过request获得url和ip
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        String url = request.getRequestURL().toString();
        String ip = request.getRemoteAddr();
        //获得类和方法名
        String classMethod = joinPoint.getSignature().getDeclaringTypeName() + "." + joinPoint.getSignature().getName();
        //获得请求的参数
        Object[] args = joinPoint.getArgs();
        //封装
        RequestLog requestLog = new RequestLog(url, ip, classMethod, args);
        logger.info("==============Before====================");
        logger.info("Request:{}",requestLog);
    }


    @AfterReturning(returning = "result",pointcut = "log()")
    public void doAfterReturn(Object result){
        logger.info("==============Result====================");
        logger.info("Result: {}" , result);
    }

    //内部类来封装这些信息
    private class RequestLog {
        private String url;
        private String ip;
        private String classMethod;
        private Object[] args;

        public RequestLog(String url, String ip, String classMethod, Object[] args) {
            this.url = url;
            this.ip = ip;
            this.classMethod = classMethod;
            this.args = args;
        }

        @Override
        public String toString() {
            return "RequestLog{" +
                    "url='" + url + '\'' +
                    ", ip='" + ip + '\'' +
                    ", classMethod='" + classMethod + '\'' +
                    ", args=" + Arrays.toString(args) +
                    '}';
        }
    }
}
