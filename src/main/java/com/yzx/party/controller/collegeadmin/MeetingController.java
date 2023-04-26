package com.yzx.party.controller.collegeadmin;

import com.yzx.party.aspect.OperationLogAnnotation;
import com.yzx.party.pojo.Admins;
import com.yzx.party.pojo.Meetings;
import com.yzx.party.pojo.News;
import com.yzx.party.service.AdminService;
import com.yzx.party.service.MeetingService;
import com.yzx.party.service.StatementService;
import com.yzx.party.util.TimeIntervalUtil;
import com.yzx.party.vo.QueryMeeting;
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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author yzx
 * @version JDK 1.8.0_131
 * @since 2022/2/7 17:37
 */
//三会一课控制器
@Controller
//这个是全局都会生效的路径
@RequestMapping("/admin/collegeAdmin")
public class MeetingController {
    @Autowired
    MeetingService meetingService;
    @Autowired
    AdminService adminService;


    //前往会议展示界面 这个是为了搜索的加载
    @GetMapping("/meetingShow")
    public String meetings(@PageableDefault(size = 5,sort = {"endtime"},direction = Sort.Direction.DESC) Pageable pageable,
                           Model model, QueryMeeting queryMeeting, HttpSession session){
        Admins loginAdmin = (Admins)session.getAttribute("admin");
        //默认只能看见学校办公室
        Integer college_id = 1;
        //获取管理员所在的学院
        if (loginAdmin!=null){
            college_id = loginAdmin.getCollege_id();
        }
        //只展示管理员所在学院的会议
        queryMeeting.setCollege_id(college_id);
        model.addAttribute("page",meetingService.listMeetings(pageable, queryMeeting));
        model.addAttribute("listAdmin",adminService.listAdmins(college_id));
        return "collegeadmin/meetingshow";
    }

    //后端的查询
    @PostMapping("/meetingShow/search")
    @OperationLogAnnotation(operaType = "搜索",operaDesc = "搜索相关会议")
    public String search(@PageableDefault(size = 5,sort = {"endtime"},direction = Sort.Direction.DESC) Pageable pageable,
                           Model model, QueryMeeting queryMeeting, HttpSession session){
        Admins loginAdmin = (Admins)session.getAttribute("admin");
        //默认只能看见学校办公室
        Integer college_id = 1;
        //获取管理员所在的学院
        if (loginAdmin!=null){
            college_id = loginAdmin.getCollege_id();
        }
        //只展示管理员所在学院的会议
        queryMeeting.setCollege_id(college_id);
        model.addAttribute("page",meetingService.listMeetings(pageable, queryMeeting));
        model.addAttribute("listAdmin",adminService.listAdmins(college_id));
        return "collegeadmin/meetingshow :: meetingSearchList";
    }


    //前往新增
    @GetMapping("/meetingInput")
    public String meetingInput(Model model){
        //区分修改和新增，防止空指针异常
        model.addAttribute("getMeetingById",new Meetings());
        return "collegeadmin/meetinginput";
    }

    //新增
    /*以为这个地方前端传递的时间不是完全的时间格式，所以要在后台处理之后再存储*/
    @PostMapping("/meetingShow")
    @OperationLogAnnotation(operaType = "新增",operaDesc = "发布会议")
    public String meetings(RedirectAttributes attributes, Meetings meetings, String startTtime,String endTtime, HttpSession session) throws ParseException {
        Admins loginAdmin = (Admins)session.getAttribute("admin");
        //默认只能看见学校办公室
        Integer college_id = 1;
        //获取管理员所在的学院
        if (loginAdmin!=null){
            college_id = loginAdmin.getCollege_id();
        }
        //处理传递的时间
        //传过来的时间格式为 “2022-02-09T10:21” 中间有一个T，所以我们要把T去掉
        SimpleDateFormat dateFormat= new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date startTime = dateFormat.parse(startTtime.replace("T"," "));
        Date endTime = dateFormat.parse(endTtime.replace("T"," "));
        //再设置时间
        meetings.setStarttime(startTime);
        meetings.setEndtime(endTime);
        //设置发布人员id
        meetings.setAdmin_id(loginAdmin.getId());
        //设置所在学院
        meetings.setCollege_id(loginAdmin.getCollege_id());
        //设置会议的持续时间
        meetings.setDuration(new TimeIntervalUtil().TimeIntervalUtilHelper(startTime,endTime));
        Meetings m = meetingService.saveMeeting(meetings);
        if (m!=null){
            //新增成功
            attributes.addFlashAttribute("message","新增会议成功");
        }else {
            //新增失败
            attributes.addFlashAttribute("message","新增会议失败");
        }
        return "redirect:/admin/collegeAdmin/meetingShow";
    }


    //前往修改
    @GetMapping("/meetingUpdate/{id}")
    public String meetingUpdate(@PathVariable Integer id, Model model){
        Meetings m = meetingService.getMeetingById(id);
        SimpleDateFormat dateFormat= new SimpleDateFormat("yyyy-MM-dd HH:mm");
        //获取数据库的时间“2022-02-09 10:21”，转化为前端需要的字符串格式的时间“2022-02-09T10:21”
        String startTtime = dateFormat.format(m.getStarttime());
        String endTtime = dateFormat.format(m.getEndtime());
        model.addAttribute("startTtime",startTtime.replace(" ","T"));
        model.addAttribute("endTtime",endTtime.replace(" ","T"));
        model.addAttribute("getMeetingById",meetingService.getMeetingById(id));
        return "collegeadmin/meetinginput";
    }


    //修改
    /*以为这个地方前端传递的时间不是完全的时间格式，所以要在后台处理之后再存储*/
    @PostMapping("/meetingUpdate/{id}")
    @OperationLogAnnotation(operaType = "修改",operaDesc = "修改会议内容")
    public String meetingUpdate(@PathVariable Integer id,Model model, RedirectAttributes attributes, Meetings meetings, String startTtime,String endTtime, HttpSession session) throws ParseException {
        Admins loginAdmin = (Admins)session.getAttribute("admin");
        //默认只能看见学校办公室
        Integer college_id = 1;
        //获取管理员所在的学院
        if (loginAdmin!=null){
            college_id = loginAdmin.getCollege_id();
        }
        //处理传递的时间
        //传过来的时间格式为 “2022-02-09T10:21” 中间有一个T，所以我们要把T去掉
        SimpleDateFormat dateFormat= new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date startTime = dateFormat.parse(startTtime.replace("T"," "));
        Date endTime = dateFormat.parse(endTtime.replace("T"," "));
        //再设置时间
        meetings.setStarttime(startTime);
        meetings.setEndtime(endTime);
        //设置发布人员id
        meetings.setAdmin_id(loginAdmin.getId());
        //设置所在学院
        meetings.setCollege_id(loginAdmin.getCollege_id());
        //设置会议的持续时间
        meetings.setDuration(new TimeIntervalUtil().TimeIntervalUtilHelper(startTime,endTime));
        Meetings m = meetingService.updateMeetings(id,meetings);
        if (m!=null){
            //新增成功
            attributes.addFlashAttribute("message","修改会议成功");
        }else {
            //新增失败
            attributes.addFlashAttribute("message","修改会议失败");
        }
        return "redirect:/admin/collegeAdmin/meetingShow";
    }


    //删除会议
    @GetMapping("/meetingDelete/{id}")
    @OperationLogAnnotation(operaType = "删除",operaDesc = "删除单个会议")
    public String meetingDelete(@PathVariable Integer id,RedirectAttributes attributes){
        meetingService.deleteMeetings(id);
        attributes.addFlashAttribute("message","删除会议成功");
        return "redirect:/admin/collegeAdmin/meetingShow";
    }

    //批量删除会议
    @GetMapping("/meetingDeleteSome/{checkedID}")
    @OperationLogAnnotation(operaType = "删除",operaDesc = "批量删除会议")
    public String meetingDeleteSome(@PathVariable String checkedID,RedirectAttributes attributes){
        //切分id，并且逐条删除
        String[] splitId = checkedID.split(",");
        for (String id:splitId){
            meetingService.deleteMeetings(Integer.parseInt(id));
        }
        attributes.addFlashAttribute("message","删除"+splitId.length+"个会议成功");
        return "redirect:/admin/collegeAdmin/meetingShow";
    }




}
