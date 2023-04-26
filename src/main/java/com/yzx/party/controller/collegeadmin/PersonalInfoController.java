package com.yzx.party.controller.collegeadmin;

import com.yzx.party.aspect.OperationLogAnnotation;
import com.yzx.party.pojo.Admins;
import com.yzx.party.service.AdminService;
import com.yzx.party.service.CollegeService;
import com.yzx.party.util.MD5Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;

/**
 * @author yzx
 * @version JDK 1.8.0_131
 * @since 2022/1/22 11:00
 */
//个人信息控制器
@Controller
//这个是全局都会生效的路径
@RequestMapping("/admin/collegeAdmin")
public class PersonalInfoController {

    @Autowired
    AdminService adminService;
    @Autowired
    CollegeService collegeService;

    //前往用户信息模块
    @GetMapping("/personalInfo")
    public String showInfo(HttpSession session, Model model){
        //获取登录的管理员
        Admins loginAdmin = (Admins)session.getAttribute("admin");
        model.addAttribute("loginAdmin",adminService.getAdminById(loginAdmin.getId()));
        model.addAttribute("collegeList",collegeService.listColleges());
        return "collegeadmin/personalinfo";
    }

    //前往更改用户信息
    @GetMapping("/personalInfoInput/{id}")
    @OperationLogAnnotation(operaType = "查看",operaDesc = "查看个人信息")
    public String infoInput(@PathVariable Integer id, HttpSession session, Model model){
        //获取登录的管理员
        Admins loginAdmin = adminService.getAdminById(id);
        model.addAttribute("collegeList",collegeService.listColleges());
        model.addAttribute("loginAdmin",loginAdmin);
        return "collegeadmin/personalinput";
    }


    //用户信息更改
    @PostMapping("/personalInfoInput/{id}")
    @OperationLogAnnotation(operaType = "修改",operaDesc = "修改个人信息")
    public String infoUpdate(@PathVariable Integer id, @RequestParam String originalpassword, RedirectAttributes attributes, Admins admins,Model model){
        MD5Util md5Util = new MD5Util();
        //原始密码
        String password = adminService.getAdminById(id).getPassword();
        //如果输入的原始密码和数据库中不匹配，那么判定错误，不可修改信息
        if (!password.equals(md5Util.md5(originalpassword)) ){
            Admins loginAdmin = adminService.getAdminById(id);
            model.addAttribute("loginAdmin",loginAdmin);
            model.addAttribute("collegeList",collegeService.listColleges());
            model.addAttribute("errorMessage","原始密码错误，不可修改信息!");
            return "collegeadmin/personalinput";
        }else {
            //如果输入新的字符串(不为空)，就是重置密码，那么就封装新的密码
            if (!"".equals(admins.getPassword())){
                //获得新密码
                String newPassword = admins.getPassword();
                //使用封装的密码
                admins.setPassword(md5Util.md5(newPassword));
            }else {
                //如果没有输入字符，那么就使用原来的密码
                admins.setPassword(password);
            }
            //设置标识，1为学院管理员
            admins.setFlag("1");
            Admins a = adminService.updateAdmins(id, admins);
            if (a!=null){
                //修改成功
                attributes.addFlashAttribute("message","修改信息成功");
            }else {
                //修改失败
                attributes.addFlashAttribute("message","修改信息失败");
            }
            return "redirect:/admin/collegeAdmin/personalInfo"; }
        }
}
