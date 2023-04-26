package com.yzx.party.controller.collegeadmin;

import com.yzx.party.aspect.OperationLogAnnotation;
import com.yzx.party.pojo.Admins;
import com.yzx.party.pojo.Mailboxs;
import com.yzx.party.pojo.Persons;
import com.yzx.party.service.CollegeService;
import com.yzx.party.service.MailBoxService;
import com.yzx.party.service.PersonService;
import com.yzx.party.util.IntroduceUtil;
import com.yzx.party.vo.QueryMail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import java.net.Inet4Address;
import java.util.Date;
import java.util.List;

/**
 * @author yzx
 * @version JDK 1.8.0_131
 * @since 2022/2/20 16:53
 */
//后端书记信箱控制器
@Controller
//这个是全局都会生效的路径
@RequestMapping("/admin/collegeAdmin")
public class CMailController {

    @Autowired
    MailBoxService mailBoxService;
    @Autowired
    PersonService personService;
    @Autowired
    CollegeService collegeService;

    /*查看本学院的全部书信*/
    @GetMapping("/mailShow")
    //先根据回复时间排序，若没有回复时间则根据发言时间排序
    public String mailShow(@PageableDefault(size = 6,sort = {"speaktime","replaytime"},direction = Sort.Direction.DESC) Pageable pageable,
                           QueryMail queryMail, Model model, HttpSession session){
        //获取登录的人员
        Admins loginAdmin = (Admins)session.getAttribute("admin");
        //只查询出属于本学院的书信
        queryMail.setCollege_id(loginAdmin.getCollege_id());
        model.addAttribute("page",mailBoxService.listMailCollege(pageable, queryMail));
        return "collegeadmin/mailshow";
    }

    //查询
    @PostMapping("/mailShow/search")
    @OperationLogAnnotation(operaType = "搜索",operaDesc = "搜索信件")
    //先根据回复时间排序，若没有回复时间则根据发言时间排序
    public String search(@PageableDefault(size = 6,sort = {"speaktime","replaytime"},direction = Sort.Direction.DESC) Pageable pageable,
                           QueryMail queryMail, Model model, HttpSession session){
        //获取登录的人员
        Admins loginAdmin = (Admins)session.getAttribute("admin");
        //只查询出属于本学院的书信
        queryMail.setCollege_id(loginAdmin.getCollege_id());
        model.addAttribute("page",mailBoxService.listMailCollege(pageable, queryMail));
        return "collegeadmin/mailshow :: mailSearchList";
    }


    //查看信件
    @GetMapping("/replayMail/{id}")
    @OperationLogAnnotation(operaType = "查看",operaDesc = "查看信件")
    public String toReplayMail(@PathVariable Integer id,Model model){
        model.addAttribute("mailById",  mailBoxService.getMailById(id));
        Mailboxs m = mailBoxService.getMailById(id);
        //只要点击过了，且原先是未读，就设置为已读；否则不变
        if ("未读".equals(m.getState())){
            m.setState("已读");
        }
        mailBoxService.updateMail(id,m);
        return "collegeadmin/mailinput";
    }


    //回复
    @PostMapping("/replayMail/{id}")
    @OperationLogAnnotation(operaType = "修改",operaDesc = "回复信件")
    public String replayMail(@PathVariable Integer id, RedirectAttributes attributes,Mailboxs mailboxs,HttpSession session){
        //获取登录
        Admins loginAdmin = (Admins)session.getAttribute("admin");
        //获取初始信息
        Mailboxs mailById = mailBoxService.getMailById(id);
        Integer person_id = mailById.getPerson_id();
        String questionintro = mailById.getQuestionintro();
        //更新
        mailboxs.setPerson_id(person_id);
        mailboxs.setQuestionintro(questionintro);
        mailboxs.setCollege_id(loginAdmin.getCollege_id());
        //更改回复时间为现在
        mailboxs.setReplaytime(new Date());
        //设置管理员信息
        mailboxs.setAdminname(loginAdmin.getName());
        //设置回答的简介
        String answer = mailboxs.getAnswer();
        mailboxs.setAnswerintro(new IntroduceUtil().IntroduceHelper(answer,10,8));
        //若是回复了，设置两个状态都为已回复
        mailboxs.setState("已回复");
        mailboxs.setStatetwo("已回复");
        Mailboxs m = mailBoxService.updateMail(id, mailboxs);
        if (m!=null){
            attributes.addFlashAttribute("message","回复信件成功");
        }else {
            attributes.addFlashAttribute("message","回复信件失败");
        }
        return "redirect:/admin/collegeAdmin/mailShow";
    }


    //单个删除
    @GetMapping("/deleteMail/{id}")
    @OperationLogAnnotation(operaType = "删除",operaDesc = "删除单条信件")
    public String deleteMail(@PathVariable Integer id,RedirectAttributes attributes){
        mailBoxService.deleteMail(id);
        attributes.addFlashAttribute("message","删除信件成功");
        return "redirect:/admin/collegeAdmin/mailShow";
    }


    //批量删除
    @GetMapping("/deleteSomeMail/{checkedID}")
    @OperationLogAnnotation(operaType = "删除",operaDesc = "批量删除信件")
    public String deleteSomeMail(@PathVariable String checkedID,RedirectAttributes attributes){
        //切分id，并且逐条删除
        String[] splitId = checkedID.split(",");
        //删除
        for (String id:splitId){
            mailBoxService.deleteMail(Integer.parseInt(id));
        }
        attributes.addFlashAttribute("message","删除"+splitId.length+"条信件成功");
        return "redirect:/admin/collegeAdmin/mailShow";
    }





}
