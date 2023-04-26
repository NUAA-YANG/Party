package com.yzx.party.controller.superadmin;

import com.yzx.party.aspect.OperationLogAnnotation;
import com.yzx.party.pojo.Admins;
import com.yzx.party.pojo.Forgets;
import com.yzx.party.service.AdminService;
import com.yzx.party.service.ForgetService;
import com.yzx.party.service.PersonService;
import com.yzx.party.util.CodeUtil;
import com.yzx.party.util.MD5Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import sun.security.provider.MD5;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.Date;

/**
 * @author yzx
 * @version JDK 1.8.0_131
 * @since 2022/1/23 11:05
 */
//用于找回密码
@Controller
//这个是全局都会生效的路径
@RequestMapping("/admin")
public class FindBackController {

    @Autowired
    AdminService adminService;
    @Autowired
    ForgetService forgetService;


    //前往找回密码页面
    @GetMapping("/toFindBack")
    public String toFindBack(Model model){
        /*防止空指针异常*/
        model.addAttribute("forgetByUsernameAndEmail",new Forgets());
        return "superadmin/findback";
    }



    //发送邮件
    @Async//可理解为多线程，减少等待时间
    @PostMapping("/forgetCode")
    public String forgetCode(@RequestParam String username,@RequestParam String email,RedirectAttributes attributes, Model model) throws MessagingException {
        Admins adminByUsernameAndEmail = adminService.getAdminByUsernameAndEmail(username, email);
        if (adminByUsernameAndEmail!=null){
            Forgets f = forgetService.sendEmail(username,email,"admin");
            System.out.println("=============邮件已发送==========");
            if (f!=null){
                model.addAttribute("forgetByUsernameAndEmail",f);
                model.addAttribute("succeedMessage","验证码已发送至邮箱，五分钟内有效，请注意查收！");
            }
            return "superadmin/findbackpost";
        }else {
            //不存在该用户
            attributes.addFlashAttribute("errorMessage","该用户名和邮箱不存在");
            return "redirect:/admin/toFindBack";
        }
    }


    //找回密码
    @PostMapping("/findBack/{id}")
    public String findBack(@PathVariable Integer id,@RequestParam String varcode,
                           @RequestParam String newPassword,RedirectAttributes attributes,
                           Model model){
        //f为数据库获取的已发送邮件对象
        Forgets databaseF = forgetService.getForgetById(id);
        //设置验证码五分钟内有效
        if (databaseF.getCreatetime().before(new Date()) && databaseF.getInvalidtime().after(new Date())){
            //获得数据库中需要修改的Admin对象
            Admins databaseAdmin = adminService.getAdminByUsernameAndEmail(databaseF.getUsername(),databaseF.getEmail());
            //需要修改的对象的id
            Integer databaseAdminId = databaseAdmin.getId();
            //匹配验证码
            if (varcode!=null && varcode.equals(databaseF.getVarcode())){
                //重置密码
                databaseAdmin.setPassword(new MD5Util().md5(newPassword));
                adminService.updateAdmins(databaseAdminId,databaseAdmin);
                model.addAttribute("newPassword",newPassword);
                model.addAttribute("username",databaseAdmin.getUsername());
                //前往修改成功的信息展示页面
                return "superadmin/findBackShow";
            }else {
                //验证码错误
                /*其实这里没用，但是为了防止空指针异常*/
                model.addAttribute("forgetByUsernameAndEmail",databaseF);
                model.addAttribute("errorMessage","验证码错误!");
                return "superadmin/findbackpost";
            }
        }else {
            //超过五分钟，验证码失效
            model.addAttribute("forgetByUsernameAndEmail",databaseF);
            attributes.addFlashAttribute("errorMessage","超过五分钟，当前验证码已失效!");
            return "redirect:/admin/toFindBack";
        }
    }


    //重定向到登录页面
    @GetMapping("/toLoginPage")
    public String toLoginPage(){
        return "redirect:/admin";
    }



}
