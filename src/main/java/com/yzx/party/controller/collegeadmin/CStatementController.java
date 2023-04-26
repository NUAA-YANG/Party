package com.yzx.party.controller.collegeadmin;

import com.yzx.party.aspect.OperationLogAnnotation;
import com.yzx.party.pojo.Admins;
import com.yzx.party.pojo.Comments;
import com.yzx.party.pojo.Statements;
import com.yzx.party.service.CommentService;
import com.yzx.party.service.MeetingService;
import com.yzx.party.service.NewService;
import com.yzx.party.service.StatementService;
import com.yzx.party.vo.QueryCS;
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
//会议发言控制器
@Controller
//这个是全局都会生效的路径
@RequestMapping("/admin/collegeAdmin")
public class CStatementController {
    @Autowired
    MeetingService meetingService;
    @Autowired
    StatementService statementService;

    //展示所有的发言
    @GetMapping("/statements")
    public String statements(@PageableDefault(size = 5,sort = {"speaktime"},direction = Sort.Direction.DESC) Pageable pageable,
                           Model model, QueryCS queryCS, HttpSession session){
        Admins loginAdmin = (Admins)session.getAttribute("admin");
        //默认只能看见学校办公室
        Integer college_id = 1;
        //获取管理员所在的学院
        if (loginAdmin!=null){
            college_id = loginAdmin.getCollege_id();
        }
        //设置只能查询到本学院的会议
        queryCS.setCollege_id(college_id);

        model.addAttribute("page",statementService.listStatements(pageable, queryCS));
        return "collegeadmin/statementshow";
    }

    //展示所有的评论
    @PostMapping("/statements/search")
    @OperationLogAnnotation(operaType = "搜索",operaDesc = "搜索相关会议发言")
    public String search(@PageableDefault(size = 5,sort = {"speaktime"},direction = Sort.Direction.DESC) Pageable pageable,
                           Model model, QueryCS queryCS, HttpSession session){
        Admins loginAdmin = (Admins)session.getAttribute("admin");
        //默认只能看见学校办公室
        Integer college_id = 1;
        //获取管理员所在的学院
        if (loginAdmin!=null){
            college_id = loginAdmin.getCollege_id();
        }
        //设置只能查询到本学院的会议
        queryCS.setCollege_id(college_id);
        model.addAttribute("page",statementService.listStatements(pageable, queryCS));
        return "collegeadmin/statementshow :: CSList";
    }


    //删除评论
    @GetMapping("/deleteStatement/{id}")
    @OperationLogAnnotation(operaType = "删除",operaDesc = "删除单条会议发言")
    public String deleteStatement(@PathVariable Integer id, RedirectAttributes attributes){
        statementService.deleteStatements(id);
        attributes.addFlashAttribute("message","删除发言成功");
        return "redirect:/admin/collegeAdmin/statements";
    }


    //批量删除评论
    @GetMapping("/deleteSomeStatement/{checkedID}")
    @OperationLogAnnotation(operaType = "删除",operaDesc = "批量删除会议发言")
    public String deleteSomeStatement(@PathVariable String checkedID, RedirectAttributes attributes){
        //切分id，并且逐条删除
        String[] splitId = checkedID.split(",");
        for (String id:splitId){
            statementService.deleteStatements(Integer.parseInt(id));
        }
        attributes.addFlashAttribute("message","删除"+splitId.length+"条发言成功");
        return "redirect:/admin/collegeAdmin/statements";
    }




}
