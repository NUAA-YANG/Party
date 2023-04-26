package com.yzx.party.aspect;

import com.yzx.party.pojo.Admins;
import com.yzx.party.pojo.Logs;
import com.yzx.party.service.CollegeService;
import com.yzx.party.service.LogService;
import com.yzx.party.util.IpUtil;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.lang.reflect.Method;
import java.util.Date;

/**
 * @author yzx
 * @version JDK 1.8.0_131
 * @since 2022/2/16 10:31
 */
@Aspect
@Component
public class OperationLogAspect {
    @Autowired
    LogService logService;
    @Autowired
    CollegeService collegeService;

    //用于匹配我们自定义的注解（就是但凡有我们自定义的注解，那么就产生日志记录到数据库）
    @Pointcut("@annotation( com.yzx.party.aspect.OperationLogAnnotation)")
    public void operaLogPoinCut() {
    }

    //获取并且保存我们的日志
    //方法返回值以及操作的日志注解
    @AfterReturning("operaLogPoinCut()")
    public void saveOperaLog(JoinPoint joinPoint) throws Throwable {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        HttpSession session = request.getSession();
        try {
            //构建单个日志对象
            Logs operationLog = new Logs();
            // 从切面织入点处通过反射机制获取织入点处的方法
            MethodSignature signature = (MethodSignature) joinPoint.getSignature();
            //获取切入点所在的方法
            Method method = signature.getMethod();
            //获取操作
            OperationLogAnnotation annotation = method.getAnnotation(OperationLogAnnotation.class);
            if (annotation != null) {
                //设置操作的类型
                operationLog.setType(annotation.operaType());
                //设置操作的描述信息
                operationLog.setDescription(annotation.operaDesc());
            }
            //操作时间
            operationLog.setOperationtime(new Date());
            //获取当前操作的用户，并且封装
            Admins loginAdmin = (Admins)session.getAttribute("admin");
            operationLog.setUsercode(loginAdmin.getName());
            //设置所在学院
            operationLog.setUsercollege(collegeService.getCollegeById(loginAdmin.getCollege_id()).getName());
            //设置访问者的ip地址
            operationLog.setIpaddress(new IpUtil().IpUtilHelper(request));
            //保存日志
            logService.saveLogs(operationLog);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
