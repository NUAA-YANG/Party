package com.yzx.party.aspect;

import java.lang.annotation.*;

/**
 * @author yzx
 * @version JDK 1.8.0_131
 * @since 2022/2/16 10:26
 */
//自定义操作日志注解
@Target(ElementType.METHOD)//注解放置的目标位置即方法级别
@Retention(RetentionPolicy.RUNTIME)//注解在哪个阶段执行
@Documented
public @interface OperationLogAnnotation {
    // 操作类型
    // 查看、搜索、登录、下载、修改、删除、新增
    String operaType() default "";
    // 操作说明，日志详情
    String operaDesc() default "";
}
