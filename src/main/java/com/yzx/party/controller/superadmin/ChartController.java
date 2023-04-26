package com.yzx.party.controller.superadmin;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yzx.party.aspect.OperationLogAnnotation;
import com.yzx.party.mapper.MeetingChartMapper;
import com.yzx.party.mapper.NewsChartMapper;
import com.yzx.party.mapper.NewsChartTwoMapper;
import com.yzx.party.mapper.PersonsTableMapper;
import com.yzx.party.pojo.Colleges;
import com.yzx.party.pojo.TaskFile;
import com.yzx.party.pojo.Tasks;
import com.yzx.party.service.ChartService;
import com.yzx.party.service.CollegeService;
import com.yzx.party.service.TaskFileService;
import com.yzx.party.service.TaskService;
import com.yzx.party.util.TaskUtil;
import com.yzx.party.vo.MeetingChart;
import com.yzx.party.vo.NewsChart;
import com.yzx.party.vo.NewsChartTwo;
import com.yzx.party.vo.PersonsShow;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author yzx
 * @version JDK 1.8.0_131
 * @since 2022/1/9 17:38
 */
//图表的展示
@Controller
//这个是全局都会生效的路径
@RequestMapping("/admin/superAdmin")
public class ChartController {


    @Autowired
    ChartService chartService;


    //树状图展示新闻发布次数
    @GetMapping("/newsChart")
    @OperationLogAnnotation(operaType = "查看",operaDesc = "查看学院新闻堆叠条形图")
    public String newsChart(Model model){
        //返回的集合顺序：学院名称、新闻发布数量、新闻评论数量、新闻阅读数量
        List<Object> listNewInfo = chartService.listNewInfo(20);
        model.addAttribute("listName",JSON.toJSONString(listNewInfo.get(0)));
        model.addAttribute("listNum",JSON.toJSONString(listNewInfo.get(1)));
        model.addAttribute("listCommentNum",JSON.toJSONString(listNewInfo.get(2)));
        model.addAttribute("listViewNum",JSON.toJSONString(listNewInfo.get(3)));
        return "superadmin/datashownews";
    }


    //表格展示党员数量
    @GetMapping("/personsChart")
    @OperationLogAnnotation(operaType = "查看",operaDesc = "查看学院党员柱状折线图")
    public String personsChart(Model model){
      //返回的集合顺序：学院名称、正式党员人数、预备党员人数、积极分子人数、总人数
        List<Object> listPersonInfo = chartService.listPersonInfo();
        //把数据转换为JSON格式
        model.addAttribute("nameJson",JSON.toJSONString(listPersonInfo.get(0)));
        model.addAttribute("offJson",JSON.toJSONString(listPersonInfo.get(1)));
        model.addAttribute("preJson",JSON.toJSONString(listPersonInfo.get(2)));
        model.addAttribute("posJson",JSON.toJSONString(listPersonInfo.get(3)));
        model.addAttribute("sumJson",JSON.toJSONString(listPersonInfo.get(4)));
        return "superadmin/datashowperson";
    }


    //折线图展示三会一课时长
    @GetMapping("/meetingsChart")
    @OperationLogAnnotation(operaType = "查看",operaDesc = "查看三会一课时长折线图")
    public String meetingsChart(Model model){
        //返回的集合顺序：学院名称、会议总时长
        List<Object> listMeetingInfo = chartService.listMeetingInfo(20);
        //把数据转换为JSON格式
        model.addAttribute("nameJson",JSON.toJSONString(listMeetingInfo.get(0)));
        model.addAttribute("timesJson",JSON.toJSONString(listMeetingInfo.get(1)));
        return "superadmin/datashowmeetings";
    }



    //任务完成情况
    @GetMapping("/tasksChart")
    @OperationLogAnnotation(operaType = "查看",operaDesc = "查看任务堆叠柱状图")
    public String tasksChart(Model model){
        //返回的集合顺序：学院名称、未完成任务数量，完成任务数量
        List<Object> listTaskInfo = chartService.listTaskInfo();
        model.addAttribute("taskCollegeNameJson",JSON.toJSONString(listTaskInfo.get(0)));
        model.addAttribute("unFinishTaskListJson",JSON.toJSONString(listTaskInfo.get(1)));
        model.addAttribute("finishedTaskListJson",JSON.toJSONString(listTaskInfo.get(2)));
        return "superadmin/datashowtasks";
    }
    
    
    
    
    
}
