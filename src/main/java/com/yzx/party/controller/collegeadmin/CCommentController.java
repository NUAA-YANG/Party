package com.yzx.party.controller.collegeadmin;

import com.yzx.party.aspect.OperationLogAnnotation;
import com.yzx.party.pojo.Admins;
import com.yzx.party.pojo.Comments;
import com.yzx.party.service.CommentService;
import com.yzx.party.service.NewService;
import com.yzx.party.vo.QueryCS;
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

/**
 * @author yzx
 * @version JDK 1.8.0_131
 * @since 2022/2/13 15:59
 */
//新闻评论控制器
@Controller
//这个是全局都会生效的路径
@RequestMapping("/admin/collegeAdmin")
public class CCommentController {
    @Autowired
    NewService newService;
    @Autowired
    CommentService commentService;

    //展示所有的评论
    @GetMapping("/comments")
    public String comments(@PageableDefault(size = 5,sort = {"creattime"},direction = Sort.Direction.DESC) Pageable pageable,
                           Model model, QueryCS queryCS, HttpSession session){
        Admins loginAdmin = (Admins)session.getAttribute("admin");
        //默认只能看见学校办公室
        Integer college_id = 1;
        //获取管理员所在的学院
        if (loginAdmin!=null){
            college_id = loginAdmin.getCollege_id();
        }
        //设置只能查询到本专业的新闻
        queryCS.setCollege_id(college_id);
        model.addAttribute("page",commentService.listComments(pageable, queryCS));
        return "collegeadmin/commentshow";
    }

    //展示所有的评论
    @PostMapping("/comments/search")
    @OperationLogAnnotation(operaType = "搜索",operaDesc = "搜索新闻评论")
    public String search(@PageableDefault(size = 5,sort = {"creattime"},direction = Sort.Direction.DESC) Pageable pageable,
                           Model model, QueryCS queryCS, HttpSession session){
        Admins loginAdmin = (Admins)session.getAttribute("admin");
        //默认只能看见学校办公室
        Integer college_id = 1;
        //获取管理员所在的学院
        if (loginAdmin!=null){
            college_id = loginAdmin.getCollege_id();
        }
        //设置只能查询到本专业的新闻
        queryCS.setCollege_id(college_id);

        model.addAttribute("page",commentService.listComments(pageable, queryCS));
        return "collegeadmin/commentshow :: CSList";
    }


    //删除评论
    @GetMapping("/deleteComment/{id}")
    @OperationLogAnnotation(operaType = "删除",operaDesc = "删除单条评论")
    public String deleteComment(@PathVariable Integer id, RedirectAttributes attributes){
        // 数据库中内置级联删除，先删除子评论，后删除父评论
        commentService.deleteCommentById(id);
        attributes.addFlashAttribute("message","删除评论成功");
        return "redirect:/admin/collegeAdmin/comments";
    }


    //批量删除评论
    @GetMapping("/deleteSomeComment/{checkedID}")
    @OperationLogAnnotation(operaType = "删除",operaDesc = "批量删除评论")
    public String deleteSomeComment(@PathVariable String checkedID, RedirectAttributes attributes){
        //切分id，并且逐条删除
        String[] splitId = checkedID.split(",");
        for (String id:splitId){
            // 数据库中内置级联删除，先删除子评论，后删除父评论
            commentService.deleteCommentById(Integer.parseInt(id));
        }
        attributes.addFlashAttribute("message","删除"+splitId.length+"条评论成功");
        return "redirect:/admin/collegeAdmin/comments";
    }
}
