package com.yzx.party.controller.personview;

import com.yzx.party.aspect.OperationLogAnnotation;
import com.yzx.party.mapper.NewsByManyMapper;
import com.yzx.party.myException.NotFoundException;
import com.yzx.party.pojo.Comments;
import com.yzx.party.pojo.News;
import com.yzx.party.pojo.Persons;
import com.yzx.party.service.*;
import com.yzx.party.util.MarkdownUtil;
import com.yzx.party.vo.NewsByMany;
import com.yzx.party.vo.QueryNew;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * @author yzx
 * @version JDK 1.8.0_131
 * @since 2022/1/15 15:53
 */
@Controller
//这个是全局都会生效的路径
@RequestMapping("/personShow")
public class IndexController {

    @Autowired
    NewService newService;
    @Autowired
    CollegeService collegeService;
    @Autowired
    PersonService personService;
    @Autowired
    AdminService adminService;
    @Autowired
    CommentService commentService;
    @Autowired
    NewsByManyMapper newsByManyMapper;

    //首页的展示
    @GetMapping("/index")
    public String index(@PageableDefault(size = 5 ,sort = {"updatetime"},direction = Sort.Direction.DESC) Pageable pageable,
                        Model model, QueryNew queryNew){
        model.addAttribute("listColleges",collegeService.listColleges());
        //作为首页只能展示的是学校发布的统一的新闻，默认学校的编号为 1
        queryNew.setCollege_id(1);
        model.addAttribute("page",newService.listNews(pageable,queryNew,"发布"));
        //获得最新发布的新闻
        model.addAttribute("listNewsUpdateTime", newsByManyMapper.listNewsByUpdateTime(1, "发布", 5));
        //获得阅读数量最多
        model.addAttribute("viewsList",newsByManyMapper.listNewsByViews(1, "发布", 5));
        //获得评论数量最多
        model.addAttribute("commentsList",newsByManyMapper.listNewsByComments(1,"发布",5));
        return "index";
    }


    //查看新闻详情
    @GetMapping("/new/{id}")
    public String newsAbout(@PathVariable Integer id, Model model, HttpSession session){
        //获取登录的人员
        Persons loginPerson = (Persons)session.getAttribute("loginPerson");
        News newsById = newService.getNewsById(id);
        if (newsById==null){
            throw new NotFoundException("该新闻不存在");
        }
        //修改阅读数量
        newService.updateViews(id,newsById);
        //================markdown转化为html开始=====================//
        //用于格式转化之后的输出
        News n = new News();
        //将对象newsById复制给n，那么后面的属性修改就不会修改数据库中的值
        BeanUtils.copyProperties(newsById,n);
        String content = n.getContent();
        n.setContent(new MarkdownUtil().markdownToHtmlExtensions(content));
        model.addAttribute("formatNew",n);
        //================markdown转化为html结束=====================//
        //携带回管理员信息,用于展示发布者名字(同时学院和学校的都有)
        model.addAttribute("adminList",adminService.listAdmins());
        //携带回去用户id，判断评论是否可以删除
        model.addAttribute("person_id",loginPerson.getId());
        //携带回去评论信息
        model.addAttribute("listCommentsParent",commentService.listCommentsParent(id));
        model.addAttribute("listCommentsSon",commentService.listCommentsSon(id));
        return "newabout";
    }


    //删除评论
    @GetMapping("/deleteComment/{id}")
    public String deleteComment(@PathVariable Integer id, RedirectAttributes attributes ){
        Comments commentById = commentService.getCommentById(id);
        //获得是属于哪个新闻
        Integer new_id = commentById.getNew_id();
        // 数据库中内置级联删除，先删除子评论，后删除父评论
        commentService.deleteCommentById(id);
        //重定向到评论区域
        return "redirect:/personShow/new/"+new_id;

    }



}
