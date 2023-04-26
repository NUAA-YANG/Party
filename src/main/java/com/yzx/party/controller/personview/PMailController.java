package com.yzx.party.controller.personview;

import com.yzx.party.pojo.Mailboxs;
import com.yzx.party.pojo.Persons;
import com.yzx.party.service.AdminService;
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
import java.util.Date;
import java.util.List;

/**
 * @author yzx
 * @version JDK 1.8.0_131
 * @since 2022/2/19 16:06
 */
@Controller
//这个是全局都会生效的路径
@RequestMapping("/personShow")
public class PMailController {
    @Autowired
    MailBoxService mailBoxService;
    @Autowired
    PersonService personService;
    @Autowired
    AdminService adminService;
    @Autowired
    CollegeService collegeService;

    /*查看个人的全部书信*/
    @GetMapping("/allMails/{stateNum}")
    //先根据回复时间排序，若没有回复时间则根据发言时间排序
    public String allMails(@PageableDefault(size = 4,sort = {"speaktime","replaytime"},direction = Sort.Direction.DESC) Pageable pageable,
                           @PathVariable Integer stateNum, QueryMail queryMail, Model model, HttpSession session){
        //获取登录的人员
        Persons loginPerson = (Persons)session.getAttribute("loginPerson");
        //只查询出属于自己的书信
        queryMail.setPerson_id(loginPerson.getId());
        //默认进入是查看已回复
        //设置state为1是已回复，2是未回复
        if (1==stateNum){
            queryMail.setStatetwo("已回复");
        }else if (2==stateNum){
            queryMail.setStatetwo("未回复");
        }
        model.addAttribute("page",mailBoxService.listMail(pageable, queryMail));
        //展示我们查看的类型，用于前台的切换
        model.addAttribute("activeState",stateNum);
        //传递本学院的管理员的信息，用于展示回复
        model.addAttribute("collegeList",collegeService.listColleges());
        return "allmails";
    }


    //查看详情
    @GetMapping("/mail/{id}")
    public String mailAbout(@PathVariable Integer id, Model model){
        model.addAttribute("mailById",mailBoxService.getMailById(id));
        return "mailabout";
    }


    //前往新增
    @GetMapping("/writeMail")
    public String writeMail(Model model){
        //防止和修改时产生空指针异常
        model.addAttribute("mailById",  new Mailboxs());
        return "writemail";
    }


    //新建
    @PostMapping("/allMails")
    public String allMails(RedirectAttributes attributes, Mailboxs mailboxs,HttpSession session){
        //获取登录的人员
        Persons loginPerson = (Persons)session.getAttribute("loginPerson");
        //设置发言时间为现在
        mailboxs.setSpeaktime(new Date());
        //设置发言信息属于本人
        mailboxs.setPerson_id(loginPerson.getId());
        mailboxs.setPersonname(loginPerson.getName());
        //设置问题简介
        String question = mailboxs.getQuestion();
        //新增的问题属于“未回复”
        mailboxs.setStatetwo("未回复");
        //给管理员的标识为“未读”
        mailboxs.setState("未读");
        //设置所属学院
        mailboxs.setCollege_id(loginPerson.getCollege_id());
        //取前50个字，裁剪出30个字作为问题简介
        mailboxs.setQuestionintro(new IntroduceUtil().IntroduceHelper(question,15,10));
        Mailboxs m = mailBoxService.saveMail(mailboxs);
        if (m!=null){
            attributes.addFlashAttribute("message","发送信件成功");
        }else {
            attributes.addFlashAttribute("message","发送信件失败");
        }
        //返回未回复界面
        return "redirect:/personShow/allMails/2";
    }


    //前往修改
    @GetMapping("/updateMail/{id}")
    public String toUpdateMail(@PathVariable Integer id,Model model){
        model.addAttribute("mailById",  mailBoxService.getMailById(id));
        return "writemail";
    }


    //修改，只有未回复的可以修改，所以是返回未回复界面
    @PostMapping("/updateMail/{id}")
    public String updateMail(@PathVariable Integer id, RedirectAttributes attributes, Mailboxs mailboxs,HttpSession session){
        //获取登录的人员
        Persons loginPerson = (Persons)session.getAttribute("loginPerson");
        //更改发言时间为现在
        mailboxs.setSpeaktime(new Date());
        //设置发言信息属于本人
        mailboxs.setPerson_id(loginPerson.getId());
        mailboxs.setPersonname(loginPerson.getName());
        //设置问题简介
        String question = mailboxs.getQuestion();
        //设置所属学院
        mailboxs.setCollege_id(loginPerson.getCollege_id());
        //修改的的问题属于“未回复”
        mailboxs.setStatetwo("未回复");
        //给管理员的标识为“未读”
        mailboxs.setState("未读");
        //取前50个字，裁剪出30个字作为问题简介
        mailboxs.setQuestionintro(new IntroduceUtil().IntroduceHelper(question,15,14));
        Mailboxs m = mailBoxService.updateMail(id,mailboxs);
        if (m!=null){
            attributes.addFlashAttribute("message","修改信件成功");
        }else {
            attributes.addFlashAttribute("message","修改信件失败");
        }
        return "redirect:/personShow/allMails/2";
    }


    //删除
    @GetMapping("/deleteMail/{id}")
    public String deleteMail(@PathVariable Integer id,RedirectAttributes attributes) {
        Mailboxs mailById = mailBoxService.getMailById(id);
        //获得标识
        String stateTwo = mailById.getStatetwo();
        mailBoxService.deleteMail(id);
        attributes.addFlashAttribute("message","删除信件成功");
        //根据不同的标识，返回不同的页面
        if ("未回复".equals(stateTwo)) {
            return "redirect:/personShow/allMails/2";
        } else if ("已回复".equals(stateTwo)) {
            return "redirect:/personShow/allMails/1";
        }
        //默认返回已回复
        return "redirect:/personShow/allMails/1";
    }


    //批量删除
    @GetMapping("/deleteSomeMail/{checkedID}")
    public String deleteSomeMail(@PathVariable String checkedID,RedirectAttributes attributes) {
        //切分id，并且逐条删除
        String[] splitId = checkedID.split(",");
        //获取第一个信件，用来标识
        Mailboxs mailById = mailBoxService.getMailById(Integer.parseInt(splitId[0]));
        //获得标识
        String stateTwo = mailById.getStatetwo();
        //先删除
        for (String id:splitId){
            mailBoxService.deleteMail(Integer.parseInt(id));
        }
        attributes.addFlashAttribute("message","删除"+splitId.length+"条信件成功");
        //根据不同的标识，返回不同的页面
        if ("未回复".equals(stateTwo)) {
            return "redirect:/personShow/allMails/2";
        } else if ("已回复".equals(stateTwo)) {
            return "redirect:/personShow/allMails/1";
        }
        //默认返回已回复
        return "redirect:/personShow/allMails/1";
    }


}
