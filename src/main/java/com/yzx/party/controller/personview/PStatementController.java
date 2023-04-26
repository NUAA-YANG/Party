package com.yzx.party.controller.personview;

import com.yzx.party.pojo.Persons;
import com.yzx.party.pojo.Statements;
import com.yzx.party.service.MeetingService;
import com.yzx.party.service.StatementService;
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
 * @since 2022/2/13 11:08
 */
//前端会议发言的控制器
@Controller
//这个是全局都会生效的路径
@RequestMapping("/personShow")
public class PStatementController {
    @Autowired
    StatementService statementService;
    @Autowired
    MeetingService meetingService;


    //展示所有的发言
    @GetMapping("/statements/{meeting_id}")
    public String statements(@PathVariable Integer meeting_id, Model model){
        //展示该会议下面的全部发言
        model.addAttribute("listStatements",statementService.listStatements(meeting_id));
        return "meetingabout :: statementListShow";
    }


    //提交发言
    @PostMapping("/statements")
    public String postStatements(Statements statements, HttpSession session){
        //获取登录的人员
        Persons loginPerson = (Persons)session.getAttribute("loginPerson");
        //设置发布时间为当前
        statements.setSpeaktime(new Date());
        //设置所属学院
        statements.setCollege_id(loginPerson.getCollege_id());
        //设置发布人员的姓名和联系方式
        statements.setNickname(loginPerson.getName());
        statements.setPhone(loginPerson.getPhone());
        statementService.saveStatement(statements);
        //重定向到发言你区域
        return "redirect:/personShow/statements/"+statements.getMeeting_id();
    }



}
