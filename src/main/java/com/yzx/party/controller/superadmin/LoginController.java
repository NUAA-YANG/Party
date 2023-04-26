package com.yzx.party.controller.superadmin;

import com.yzx.party.aspect.OperationLogAnnotation;
import com.yzx.party.pojo.Admins;
import com.yzx.party.service.AdminService;
import com.yzx.party.service.CollegeService;
import com.yzx.party.util.MD5Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;

/**
 * @author yzx
 * @version JDK 1.8.0_131
 * @since 2022/1/5 15:43
 */
//登录的控制器
@Controller
//这个是全局都会生效的路径
@RequestMapping("/admin")
public class LoginController {
    @Autowired
    AdminService adminService;
    @Autowired
    CollegeService collegeService;

    //默认不输入也是登录页面
    @GetMapping()
    public String loginPage(){
        return "superadmin/login";
    }

    //学校管理员首页
    @GetMapping("/superAdmin/index")
    public String schoolIndex(Model model){
        model.addAttribute("listCollege",collegeService.listColleges());
        return "superadmin/index";
    }

    //学院管理员首页
    @GetMapping("/collegeAdmin/index")
    public String collegeIndex(Model model){
        model.addAttribute("listCollege",collegeService.listColleges());
        return "collegeadmin/index"; }

    //验证登录
    @PostMapping("/login")
    public String login(@RequestParam String username,
                        @RequestParam String password,
                        HttpSession session,
                        RedirectAttributes attributes,
                        Model model){
        //验证的是加密之后的密码
        MD5Util md5Util = new MD5Util();
        Admins admin = adminService.checkAdmin(username, md5Util.md5(password));
        if (admin!=null){
            session.setAttribute("admin",admin);
            //学校管理员标志位2
            if ("2".equals(admin.getFlag())){
                model.addAttribute("listCollege",collegeService.listColleges());
                return "superadmin/index";
            }else {
                model.addAttribute("listCollege",collegeService.listColleges());
                return "collegeadmin/index";
            }
        }else {
            //这里要注意，为什么不能用Model来设置，因为这里使用的是重定向，那么Model会清除
            attributes.addFlashAttribute("message","用户名或者密码错误");
            return "redirect:/admin";
        }
    }


    //用户注销
    @GetMapping("/logout")
    public String logout(HttpSession session){
        session.removeAttribute("admin");
        return "redirect:/admin";
    }


}
