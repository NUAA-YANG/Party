package com.yzx.party.controller.collegeadmin;

import com.alibaba.fastjson.JSON;
import com.yzx.party.aspect.OperationLogAnnotation;
import com.yzx.party.mapper.NewsChartMapper;
import com.yzx.party.pojo.Admins;
import com.yzx.party.pojo.News;
import com.yzx.party.service.CollegeService;
import com.yzx.party.service.MajorService;
import com.yzx.party.service.NewService;
import com.yzx.party.util.IntroduceUtil;
import com.yzx.party.vo.NewsChart;
import com.yzx.party.vo.QueryNew;
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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author yzx
 * @version JDK 1.8.0_131
 * @since 2022/1/10 11:16
 */
//发布新闻控制器
@Controller
//这个是全局都会生效的路径
@RequestMapping("/admin/collegeAdmin")
public class CNewsController {

    @Autowired
    NewService newService;
    @Autowired
    CollegeService collegeService;
    @Autowired
    MajorService majorService;
    @Autowired
    NewsChartMapper newsChartMapper;

    //树状图新闻展示
    @GetMapping("/newsTree")
    @OperationLogAnnotation(operaType = "查看",operaDesc = "查看专业新闻树状图")
    public String newsTree(Model model,HttpSession session){
        Admins loginAdmin = (Admins)session.getAttribute("admin");
        //默认只能看见学校办公室
        Integer college_id = 1;
        //获取管理员所在的学院
        if (loginAdmin!=null){
            college_id = loginAdmin.getCollege_id();
        }
        //获取树状图的所有学院信息
        List<NewsChart> newsTreeCharts = newsChartMapper.foreachCollegeNews(college_id, 10);
        //将专业名字和发布新闻的数量取出
        List<String> listName = new ArrayList<>();
        List<Integer> listNums = new ArrayList<>();
        for (NewsChart newsChart:newsTreeCharts){
            listName.add(newsChart.getName());
            listNums.add(newsChart.getNums());
        }

        //把数据转换为JSON格式
        model.addAttribute("nameJson", JSON.toJSONString(listName));
        model.addAttribute("numsJson",JSON.toJSONString(listNums));

        return "collegeadmin/newstreechart";
    }


    //前往新闻展示界面 这个是为了搜索的加载
    @GetMapping("/newshow")
    public String news(@PageableDefault(size = 6,sort = {"updatetime"},direction = Sort.Direction.DESC) Pageable pageable,
                       Model model, QueryNew queryNew, HttpSession session){
        Admins loginAdmin = (Admins)session.getAttribute("admin");
        //默认只能看见学校办公室
        Integer college_id = 1;
        //获取管理员所在的学院
        if (loginAdmin!=null){
            college_id = loginAdmin.getCollege_id();
        }
        //用于筛选专业
        model.addAttribute("listMajors",majorService.listMajors(college_id));
        //只展示管理员所在学院的新闻
        queryNew.setCollege_id(college_id);
        model.addAttribute("page",newService.listNewsByCollegeId(pageable,queryNew));
        return "collegeadmin/newshow";
    }

    //后端的新闻查询
    @PostMapping("/newshow/search")
    @OperationLogAnnotation(operaType = "搜索",operaDesc = "搜索相关新闻")
    public String search(@PageableDefault(size = 6,sort = {"updatetime"},direction = Sort.Direction.DESC) Pageable pageable,
                       Model model, QueryNew queryNew, HttpSession session){
        Admins loginAdmin = (Admins)session.getAttribute("admin");
        //默认只能看见学校办公室
        Integer college_id = 1;
        //获取管理员所在的学院
        if (loginAdmin!=null){
            college_id = loginAdmin.getCollege_id();
        }
        //用于筛选专业
        model.addAttribute("listMajors",majorService.listMajors(college_id));
        //只展示管理员所在学院的新闻
        queryNew.setCollege_id(college_id);
        model.addAttribute("page",newService.listNewsByCollegeId(pageable,queryNew));
        return "collegeadmin/newshow :: newSearchList";
    }

    //前往新增
    @GetMapping("/newsInput")
    public String newInput(Model model,HttpSession session){
        Admins loginAdmin = (Admins)session.getAttribute("admin");
        //默认只能看见学校办公室
        Integer college_id = 1;
        //获取管理员所在的学院
        if (loginAdmin!=null){
            college_id = loginAdmin.getCollege_id();
        }
        //用于筛选专业
        model.addAttribute("listMajors",majorService.listMajors(college_id));
        //为了防止和修改的时候产生的空指针异常，先传一个空的对象过去
        model.addAttribute("getNewById",new News());
        return "collegeadmin/newsinput";
    }

    //新增
    @PostMapping("/newshow")
    @OperationLogAnnotation(operaType = "新增",operaDesc = "发布新闻")
    public String news(Model model, RedirectAttributes attributes, News news,HttpSession session){
        Admins loginAdmin = (Admins)session.getAttribute("admin");
        //默认只能看见学校办公室
        Integer college_id = 1;
        //获取管理员所在的学院
        if (loginAdmin!=null){
            college_id = loginAdmin.getCollege_id();
        }
        //设置更新时间为现在
        news.setUpdatetime(new Date());
        //设置为管理员所在的学院
        news.setCollege_id(college_id);
        //新建的时候浏览次数为0
        news.setViews(0);
        //设置发布人员的id
        news.setAdmin_id(loginAdmin.getId());
        //取前100个字，截取产生文章的介绍
        news.setIntroduce(new IntroduceUtil().IntroduceHelper(news.getContent(),100,80));
        News n = newService.saveNews(news);
        if (n!=null){
            //新增成功
            attributes.addFlashAttribute("message","新增新闻成功");
        }else {
            //新增失败
            attributes.addFlashAttribute("message","新增新闻失败");
        }

        return "redirect:/admin/collegeAdmin/newshow";
    }


    //前往修改
    @GetMapping("/newsUpdate/{id}")
    public String toNewUpdate(@PathVariable Integer id,Model model,HttpSession session){
        Admins loginAdmin = (Admins)session.getAttribute("admin");
        //默认只能看见学校办公室
        Integer college_id = 1;
        //获取管理员所在的学院
        if (loginAdmin!=null){
            college_id = loginAdmin.getCollege_id();
        }
        //用于筛选专业
        model.addAttribute("listMajors",majorService.listMajors(college_id));
        model.addAttribute("getNewById",newService.getNewsById(id));
        return "collegeadmin/newsinput";
    }

    //修改新闻
    @PostMapping("/newsUpdate/{id}")
    @OperationLogAnnotation(operaType = "修改",operaDesc = "修改新闻内容")
    public String newUpdate(@PathVariable Integer id,Model model,
                            RedirectAttributes attributes, News news,
                            HttpSession session){
        Admins loginAdmin = (Admins)session.getAttribute("admin");
        //默认只能看见学校办公室
        Integer college_id = 1;
        //获取管理员所在的学院
        if (loginAdmin!=null){
            college_id = loginAdmin.getCollege_id();
        }
        //设置更新时间为现在
        news.setUpdatetime(new Date());
        news.setCollege_id(college_id);
        //设置发布人员的id
        news.setAdmin_id(loginAdmin.getId());
        //不修改浏览次数
        news.setViews(newService.getNewsById(id).getViews());
        //取前100个字，截取产生文章的介绍
        news.setIntroduce(new IntroduceUtil().IntroduceHelper(news.getContent(),100,80));
        News n = newService.updateNews(id, news);
        if (n!=null){
            //新增成功
            attributes.addFlashAttribute("message","修改新闻成功");
        }else {
            //新增失败
            attributes.addFlashAttribute("message","修改新闻失败");
        }
        return "redirect:/admin/collegeAdmin/newshow";
    }

    //删除新闻
    @GetMapping("/newsDelete/{id}")
    @OperationLogAnnotation(operaType = "删除",operaDesc = "删除单篇新闻")
    public String newDelete(@PathVariable Integer id, RedirectAttributes attributes){
        newService.deleteNews(id);
        attributes.addFlashAttribute("message","删除新闻成功");
        return "redirect:/admin/collegeAdmin/newshow";
    }


    //批量删除新闻
    @GetMapping("/newsDeleteSome/{checkedID}")
    @OperationLogAnnotation(operaType = "删除",operaDesc = "批量删除新闻")
    public String newsDeleteSome(@PathVariable String checkedID, RedirectAttributes attributes){
        //切分id，并且逐条删除
        String[] splitId = checkedID.split(",");
        for (String id:splitId){
            newService.deleteNews(Integer.parseInt(id));
        }
        attributes.addFlashAttribute("message","删除"+splitId.length+"篇新闻成功");
        return "redirect:/admin/collegeAdmin/newshow";
    }






}
