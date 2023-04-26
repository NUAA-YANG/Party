package com.yzx.party.controller.personview;

import com.yzx.party.mapper.NewsByManyMapper;
import com.yzx.party.pojo.Persons;
import com.yzx.party.service.CollegeService;
import com.yzx.party.service.NewService;
import com.yzx.party.service.PersonService;
import com.yzx.party.util.MD5Util;
import com.yzx.party.vo.QueryNew;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;

/**
 * @author yzx
 * @version JDK 1.8.0_131
 * @since 2022/1/15 16:39
 */
//登录的控制器
@Controller
//这个是全局都会生效的路径
@RequestMapping("/personShow")
public class PLoginController {
    @Autowired
    PersonService personService;
    @Autowired
    NewService newService;
    @Autowired
    CollegeService collegeService;
    @Autowired
    NewsByManyMapper newsByManyMapper;

    //默认不输入也是登录页面
    @GetMapping()
    public String loginPage(){
        return "plogin";
    }
    
    @PostMapping("/login")
    public String login(@RequestParam String username,
                        @RequestParam String password,
                        HttpSession session,
                        RedirectAttributes attributes,
                        Model model,
                        @PageableDefault(size = 5,sort = {"updatetime"},direction = Sort.Direction.DESC) Pageable pageable,
                        QueryNew queryNew){
        //验证的是加密之后的密码
        MD5Util md5Util = new MD5Util();
        Persons p = personService.getPersonsByUsernameAndPassword(username, md5Util.md5(password));
        if (p!=null){
            //存入session
            session.setAttribute("loginPerson",p);
            //携带学院信息
            model.addAttribute("listColleges",collegeService.listColleges());
            //携带新闻信息，默认学校是1
            queryNew.setCollege_id(1);
            model.addAttribute("page",newService.listNews(pageable,queryNew,"发布"));
            //携带最新发布
            model.addAttribute("listNewsUpdateTime", newsByManyMapper.listNewsByUpdateTime(1, "发布", 5));
            //获得阅读数量最多
            model.addAttribute("viewsList",newsByManyMapper.listNewsByViews(1, "发布", 5));
            //获得评论数量最多
            model.addAttribute("commentsList",newsByManyMapper.listNewsByComments(1,"发布",5));
            return "index";
        }else {
            //这里要注意，为什么不能用Model来设置，因为这里使用的是重定向，那么Model会清除
            attributes.addFlashAttribute("message","用户名或者密码错误");
            return "redirect:/personShow";
        }

    }



    //用户注销
    @GetMapping("/logout")
    public String logout(HttpSession session){
        session.removeAttribute("loginPerson");
        return "redirect:/personShow";
    }
}
