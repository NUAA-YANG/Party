package com.yzx.party.controller.superadmin;

import com.sun.org.apache.xpath.internal.operations.Mod;
import com.yzx.party.aspect.OperationLogAnnotation;
import com.yzx.party.pojo.Admins;
import com.yzx.party.service.AdminService;
import com.yzx.party.service.CollegeService;
import com.yzx.party.util.MD5Util;
import com.yzx.party.vo.QueryAdmin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

/**
 * @author yzx
 * @version JDK 1.8.0_131
 * @since 2022/1/8 14:45
 */
//后台管理员
@Controller
//这个是全局都会生效的路径
@RequestMapping("/admin/superAdmin")
public class AdminController {
    @Autowired
    AdminService adminService;
    @Autowired
    CollegeService collegeService;

    //管理员展示
    @GetMapping("/administrators")
    public String administrators(@PageableDefault(size = 5,sort = {"id"},direction = Sort.Direction.DESC) Pageable pageable,
                                 Model model, QueryAdmin queryAdmin,HttpSession session){
        Admins adminLog = (Admins)session.getAttribute("admin");
        //把登录的管理员信息传到前台，保证自己不能删除自己
        model.addAttribute("adminLog",adminLog);
        model.addAttribute("listColleges",collegeService.listColleges());
        model.addAttribute("page",adminService.listAdmins(pageable, queryAdmin));
        return "superadmin/administrators";
    }

    //后端管理员筛选
    @PostMapping("/administrators/search")
    @OperationLogAnnotation(operaType = "搜索",operaDesc = "搜索学院管理员")
    public String search(@PageableDefault(size = 5,sort = {"id"},direction = Sort.Direction.DESC) Pageable pageable,
                                 Model model, QueryAdmin queryAdmin,HttpSession session){
        Admins adminLog = (Admins)session.getAttribute("admin");
        //把登录的管理员信息传到前台，保证自己不能删除自己
        model.addAttribute("adminLog",adminLog);
        model.addAttribute("listColleges",collegeService.listColleges());
        model.addAttribute("page",adminService.listAdmins(pageable, queryAdmin));
        return "superadmin/administrators :: adminsList";
    }


    //个人信息展示
    @GetMapping("/personalInfo")
    @OperationLogAnnotation(operaType = "查看",operaDesc = "查看个人信息")
    public String toPersonalInfo(Model model,HttpSession session){
        Admins loginAdmin = (Admins)session.getAttribute("admin");
        model.addAttribute("collegeList",collegeService.listColleges());
        model.addAttribute("loginAdmin",adminService.getAdminById(loginAdmin.getId()));
        return "superadmin/personalinfo";
    }


    //前往新增
    @GetMapping("/administratorsInput")
    public String toAdministratorsInput(Model model){
        model.addAttribute("admins",new Admins());
        model.addAttribute("listColleges",collegeService.listColleges());
        return "superadmin/administratorsinput";
    }

    //新增
    @PostMapping("/administrators")
    @OperationLogAnnotation(operaType = "新增",operaDesc = "新增学院管理员")
    public String AdministratorsInput(@Valid Admins admins, BindingResult result, @RequestParam String originalpassword,RedirectAttributes attributes,Model model){
        //只校验用户名重复
        if (adminService.getAdminByUsername(admins.getUsername())!=null){
            result.rejectValue("username","nameError","该用户名已经存在");
        }
        //校验失败重新返回
        if (result.hasErrors()){
            model.addAttribute("listColleges",collegeService.listColleges());
            return "superadmin/administratorsinput";
        }
        //如果添加的是学校的管理员
        if(admins.getCollege_id()==1){
            admins.setFlag("2");
        }else {
            //默认是添加的学院管理员，标志位为1
            admins.setFlag("1");
        }
        //加密
        admins.setPassword(new MD5Util().md5(originalpassword));
        Admins a = adminService.saveAdmins(admins);
        if (a!=null){
            //新增成功
            attributes.addFlashAttribute("message","新增学院管理员成功");
        }else {
            //新增失败
            attributes.addFlashAttribute("message","新增学院管理员失败");
        }
        return "redirect:/admin/superAdmin/administrators";
    }

    //前往修改
    @GetMapping("/administratorsUpdate/{id}")
    public String toAdministratorsUpdate(@PathVariable Integer id,Model model){
        model.addAttribute("admins",adminService.getAdminById(id));
        model.addAttribute("listColleges",collegeService.listColleges());
        return "superadmin/administratorsinput";
    }

    //修改
    @PostMapping("/administratorsUpdate/{id}")
    @OperationLogAnnotation(operaType = "修改",operaDesc = "修改学院管理员信息")
    public String administratorsUpdate(@Valid Admins admins, BindingResult result, @PathVariable Integer id, @RequestParam String originalpassword,RedirectAttributes attributes, HttpSession session,Model model){
        MD5Util md5Util = new MD5Util();
        //原始密码
        String password = adminService.getAdminById(id).getPassword();
        //如果输入的原始密码和数据库中不匹配，那么判定错误，不可修改信息
        if (!password.equals(md5Util.md5(originalpassword)) ){
            model.addAttribute("admins",adminService.getAdminById(id));
            model.addAttribute("listColleges",collegeService.listColleges());
            model.addAttribute("errorMessage","原始密码错误，不可修改信息!");
            return "superadmin/administratorsinput";
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
        }
        //如果添加的是学校的管理员
        if(admins.getCollege_id()==1){
            admins.setFlag("2");
        }else {
            //默认是添加的学院管理员，标志位为1
            admins.setFlag("1");
        }
        Admins a = adminService.updateAdmins(id, admins);
        if (a!=null){
            //修改成功
            attributes.addFlashAttribute("message","修改管理员信息成功");
        }else {
            //修改失败
            attributes.addFlashAttribute("message","修改管理员信息失败");
        }


        //如果修改的是个人信息，那么返回个人信息页面
        Admins loginAdmin = (Admins)session.getAttribute("admin");
        if (loginAdmin.getId()==admins.getId()){
            return "redirect:/admin/superAdmin/personalInfo";
        }else {
            //否则修改的是别的管理员，返回管理员列表
            return "redirect:/admin/superAdmin/administrators";
        }
    }

    //删除
    @GetMapping("/administratorsDelete/{id}")
    @OperationLogAnnotation(operaType = "删除",operaDesc = "删除学院管理员")
    public String administratorsDelete(@PathVariable Integer id,RedirectAttributes attributes){
        adminService.deleteAdmins(id);
        attributes.addFlashAttribute("message","删除学院管理员成功");
        return "redirect:/admin/superAdmin/administrators";
    }

}
