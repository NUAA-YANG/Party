package com.yzx.party.service;

import com.yzx.party.dao.ForgetRepository;
import com.yzx.party.pojo.Forgets;
import com.yzx.party.util.CodeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.transaction.Transactional;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author yzx
 * @version JDK 1.8.0_131
 * @since 2022/3/14 15:59
 */
@Service
public class ForgetServiceImpl implements ForgetService {

    @Autowired
    ForgetRepository forgetRepository;
    @Autowired
    JavaMailSenderImpl mailSender;
    //获得发件人的邮箱
    @Value("${spring.mail.username}")
    private String sender;

    @Transactional
    @Override
    public Forgets saveForget(Forgets forgets) {
        return forgetRepository.save(forgets);
    }

    @Transactional
    @Override
    public Forgets sendEmail(String username, String email,String flag) throws MessagingException {
        //调用自定义的验证码生成类工具,生成六位的验证码
        String varCode = new CodeUtil().CodeUtilHelper(6);
        /*一封复杂的邮件*/
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        /*组装*/
        MimeMessageHelper message = new MimeMessageHelper(mimeMessage,true);
        // 设置邮件标题
        message.setSubject("智慧党建系统找回密码-验证码");
        // 设置邮件正文,为 true才能解析HTML元素,如果为 false会解析为字符串
        message.setText("尊敬的"+username+",您好:\n"
                + "\n本次请求的邮件验证码为:" + varCode + ",本验证码5分钟内有效,请及时输入。（请勿泄露此验证码）\n"
                + "\n如非本人操作，请忽略该邮件。\n(这是一封自动发送的邮件，请不要直接回复）");
        message.setTo(email);	//设置收件人
        message.setFrom(sender);	//设置发件人
        try {
            mailSender.send(mimeMessage);	//发送邮件
        }catch (Exception e){
            e.getMessage();
        }

        //记录存到数据库中
        Forgets forgets = new Forgets();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        //设置发送时间和过期时间
        Date nowTime = new Date();
        Date invalidTime = new Date(nowTime.getTime() + 300000);//当前时间之后的五分钟时间
        forgets.setCreatetime(nowTime);
        forgets.setInvalidtime(invalidTime);
        forgets.setEmail(email);
        forgets.setUsername(username);
        forgets.setVarcode(varCode);
        //设置区分管理员和用户
        forgets.setFlag(flag);
        return forgetRepository.save(forgets);

    }

    @Override
    public Forgets getForgetByUsernameAndEmail(String username, String email) {
        return forgetRepository.findByUsernameAndEmail(username, email);
    }

    @Override
    public Forgets getForgetById(Integer id) {
        return forgetRepository.findById(id).get();
    }
}
