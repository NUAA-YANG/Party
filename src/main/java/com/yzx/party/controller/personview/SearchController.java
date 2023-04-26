package com.yzx.party.controller.personview;

import com.yzx.party.pojo.News;
import com.yzx.party.pojo.Persons;
import com.yzx.party.service.CollegeService;
import com.yzx.party.service.NewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * @author yzx
 * @version JDK 1.8.0_131
 * @since 2022/1/20 11:38
 */
//搜索
@Controller
//这个是全局都会生效的路径
@RequestMapping("/personShow")
public class SearchController {

    @Autowired
    NewService newService;
    @Autowired
    CollegeService collegeService;

    @PostMapping("/search")
    public String search(@PageableDefault(size = 4,sort = {"id"},direction = Sort.Direction.ASC) Pageable pageable,
                         @RequestParam String queryWord, Model model, HttpSession session){
        //获取登录的人员
        Persons loginPerson = (Persons)session.getAttribute("loginPerson");
        //默认只能看见学校
        Integer college_id = 1;
        //获取登入者所在的学院
        if (loginPerson!=null){
            college_id = loginPerson.getCollege_id();
        }

        //只搜索发布了的新闻，只在学校或者学院新闻中根据新闻的标题和简介模糊查询
        Page<News> pageInfo = newService.listNewSearch("%"+queryWord+"%", college_id,  "发布",pageable);
        model.addAttribute("listColleges",collegeService.listColleges());
        model.addAttribute("queryWord",queryWord);
        model.addAttribute("page",pageInfo);
        System.out.println("==============================");
        System.out.println(college_id+":"+queryWord);
        List<News> contentTest = pageInfo.getContent();
        contentTest.forEach(x-> System.out.println(x.getId()+":"+x.getTitle()));
        System.out.println("==============================");
        return "search";
    }
}
