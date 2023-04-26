package com.yzx.party.service;

import com.yzx.party.pojo.Forgets;

import javax.mail.MessagingException;

/**
 * @author yzx
 * @version JDK 1.8.0_131
 * @since 2022/3/14 15:57
 */

//用于找回密码
public interface ForgetService {
    //保存记录
    Forgets saveForget(Forgets forgets);
    //发送邮件,参数为用户名，找回邮箱已经身份区分（admin或person），信息保存到数据库
    Forgets sendEmail(String username, String email,String flag) throws MessagingException;
    //查询验证
    Forgets getForgetByUsernameAndEmail(String username, String email);
    Forgets getForgetById(Integer id);
}
