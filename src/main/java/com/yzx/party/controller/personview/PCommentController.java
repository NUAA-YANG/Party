package com.yzx.party.controller.personview;

import com.yzx.party.pojo.Comments;
import com.yzx.party.pojo.Meetings;
import com.yzx.party.pojo.Persons;
import com.yzx.party.service.CommentService;
import com.yzx.party.service.MeetingService;
import com.yzx.party.service.NewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.List;

/**
 * @author yzx
 * @version JDK 1.8.0_131
 * @since 2022/1/18 11:40
 */
//前端新闻评论的控制器
@Controller
//这个是全局都会生效的路径
@RequestMapping("/personShow")
public class PCommentController {
    @Autowired
    CommentService commentService;
    @Autowired
    NewService newService;
    //展示新闻评论
    @GetMapping("/comments/{new_id}")
    public String comments(@PathVariable Integer new_id, Model model,HttpSession session){
        //获取登录的人员
        Persons loginPerson = (Persons)session.getAttribute("loginPerson");
        //携带回去用户id，判断评论是否可以删除
        model.addAttribute("person_id",loginPerson.getId());
        //所有的父级评论
        List<Comments> listCommentsParent = commentService.listCommentsParent(new_id);
        //所有的子级评论
        List<Comments> listCommentsSon = commentService.listCommentsSon(new_id);
        model.addAttribute("listCommentsParent",listCommentsParent);
        model.addAttribute("listCommentsSon",listCommentsSon);
        return "newabout :: commentListShow";
    }

    //提交评论
    @PostMapping("/comments")
    public String postComments(Comments comment, HttpSession session){
        //获取登录的人员
        Persons loginPerson = (Persons)session.getAttribute("loginPerson");
        //根据前端传递的parent_id来判断存储
        //如果是-1 那么表示是最顶端的评论；否则就是回复的评论
        Integer parent_id = comment.getParent_id();
        if(parent_id==-1){
            comment.setParent_id(null);
        }
        comment.setCreattime(new Date());
        //设置所属学院
        comment.setCollege_id(newService.getNewsById(comment.getNew_id()).getCollege_id());
        //设置发布者的姓名和联系方式
        comment.setNickname(loginPerson.getName());
        comment.setPhone(loginPerson.getPhone());
        comment.setPerson_id(loginPerson.getId());
        Comments c = commentService.saveComment(comment);
        //重定向到评论区域
        return "redirect:/personShow/comments/"+comment.getNew_id();
    }
}
