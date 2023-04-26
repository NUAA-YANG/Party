package com.yzx.party.controller.personview;

import com.yzx.party.mapper.NewsChartMapper;
import com.yzx.party.pojo.*;
import com.yzx.party.service.AdminService;
import com.yzx.party.service.CollegeService;
import com.yzx.party.service.MajorService;
import com.yzx.party.service.NewService;
import com.yzx.party.vo.NewsChart;
import com.yzx.party.vo.QueryNew;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * @author yzx
 * @version JDK 1.8.0_131
 * @since 2022/1/19 10:53
 */
//学院新闻模块
@Controller
//这个是全局都会生效的路径
@RequestMapping("/personShow")
public class CollegeNewsController {
    @Autowired
    NewService newService;
    @Autowired
    CollegeService collegeService;
    @Autowired
    MajorService majorService;
    @Autowired
    NewsChartMapper newsChartMapper;
    @Autowired
    AdminService adminService;

    @GetMapping("/collegenews/majors/{major_id}")
    public String majorShow(@PageableDefault(size = 4,sort = {"updatetime"},direction = Sort.Direction.DESC) Pageable pageable,
                            @PathVariable Integer major_id, HttpSession session, Model model, QueryNew queryNew){
        //获取登录的人员
        Persons loginPerson = (Persons)session.getAttribute("loginPerson");
        //默认只能看见学校
        Integer college_id = 1;
        //获取登入者所在的学院
        if (loginPerson!=null){
            college_id = loginPerson.getCollege_id();
        }
        //获取登录者所在学院的名字
        String college_name = collegeService.getCollegeById(college_id).getName();
        //查询登录者所在学院的全部专业
        List<Majors> majorsList = majorService.listMajors(college_id);
        //查询登录者所在学院的全部专业所发布的新闻数量
        List<NewsChart> majorsNewsList = newsChartMapper.foreachCollegeNews(college_id, 10);
        //获取学院的管理员，用于赋值名字
        List<Admins> adminsList = adminService.listAdmins(college_id);
        //如果是从首页进去的，那么直接默认查询第一个分类
        if (major_id==-1){
            if (majorsList.get(0)!=null){
                major_id = majorsList.get(0).getId();
            }
        }
        //获取对应学院和对应专业的新闻
        queryNew.setCollege_id(college_id);
        queryNew.setMajor_id(major_id);
        Page<News> pageInfo = newService.listNews(pageable, queryNew, "发布");
        //携带新闻信息
        model.addAttribute("page",pageInfo);
        //携带专业信息
        model.addAttribute("majorsList",majorsList);
        //携带专业信息以及对应的发布数量
        model.addAttribute("majorsNewsList",majorsNewsList);
        //携带高亮专业信息
        model.addAttribute("activeMajorId",major_id);
        //学院名字
        model.addAttribute("college_name",college_name);
        //学院的管理员名字
        model.addAttribute("adminsList",adminsList);
        return "collegenews";
    }

}
