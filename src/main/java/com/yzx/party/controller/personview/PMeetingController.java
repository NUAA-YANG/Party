package com.yzx.party.controller.personview;

import com.yzx.party.myException.NotFoundException;
import com.yzx.party.pojo.Admins;
import com.yzx.party.pojo.Meetings;
import com.yzx.party.pojo.News;
import com.yzx.party.pojo.Persons;
import com.yzx.party.service.AdminService;
import com.yzx.party.service.MeetingService;
import com.yzx.party.service.StatementService;
import com.yzx.party.util.MarkdownUtil;
import com.yzx.party.vo.QueryMeeting;
import org.springframework.beans.BeanUtils;
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

import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * @author yzx
 * @version JDK 1.8.0_131
 * @since 2022/2/9 17:39
 */
@Controller
//这个是全局都会生效的路径
@RequestMapping("/personShow")
public class PMeetingController {

    @Autowired
    MeetingService meetingService;
    @Autowired
    AdminService adminService;
    @Autowired
    StatementService statementService;


    //前端展示所有的会议
    @GetMapping("/allMeetings/{mtype}")
    public String allMeetings(@PageableDefault(size = 4,sort = {"endtime"},direction = Sort.Direction.DESC) Pageable pageable,
                              @PathVariable Integer mtype, Model model, QueryMeeting queryMeeting, HttpSession session){
        //获取登录的人员
        Persons loginPerson = (Persons)session.getAttribute("loginPerson");
        //默认只能看见学校
        Integer college_id = 1;
        //获取登入者所在的学院
        if (loginPerson!=null){
            college_id = loginPerson.getCollege_id();
        }
        queryMeeting.setCollege_id(college_id);
        //查询出相关的会议
        if (1==mtype){
            queryMeeting.setMtype("支部党员大会");
        }else if (2==mtype){
            queryMeeting.setMtype("党小组会");
        }else if (3==mtype){
            queryMeeting.setMtype("支委会");
        }else if (4==mtype){
            queryMeeting.setMtype("党课");
        }else if (5==mtype){
            queryMeeting.setMtype("其他会议");
        }
        model.addAttribute("page",meetingService.listMeetings(pageable, queryMeeting));
        model.addAttribute("activeMtype",mtype);
        model.addAttribute("listAdmin",adminService.listAdmins(college_id));
        return "allmeetings";
    }


   //查看详情
    @GetMapping("/meeting/{id}")
    public String meetingAbout(@PathVariable Integer id, Model model,HttpSession session){
        //获取登录的人员
        Persons loginPerson = (Persons)session.getAttribute("loginPerson");
        Meetings meetingById = meetingService.getMeetingById(id);
        if (meetingById==null){
            throw new NotFoundException("该会议不存在");
        }
        //================markdown转化为html开始=====================//
        //用于格式转化之后的输出
        Meetings m = new Meetings();
        //将对象newsById复制给n，那么后面的属性修改就不会修改数据库中的值
        BeanUtils.copyProperties(meetingById,m);
        String content = m.getContent();
        m.setContent(new MarkdownUtil().markdownToHtmlExtensions(content));
        model.addAttribute("formatMeeting",m);
        //================markdown转化为html结束=====================//
        //携带回管理员信息,用于展示发布者名字
        model.addAttribute("adminList",adminService.listAdmins(loginPerson.getCollege_id()));
        //展示发言信息
        model.addAttribute("listStatements",statementService.listStatements(id));
        return "meetingabout";
    }




}
