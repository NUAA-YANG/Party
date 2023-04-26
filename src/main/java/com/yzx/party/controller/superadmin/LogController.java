package com.yzx.party.controller.superadmin;

import com.yzx.party.aspect.OperationLogAnnotation;
import com.yzx.party.mapper.DeleteLogBySizeMapper;
import com.yzx.party.pojo.Admins;
import com.yzx.party.pojo.Logs;
import com.yzx.party.service.CollegeService;
import com.yzx.party.service.CommentService;
import com.yzx.party.service.LogService;
import com.yzx.party.service.NewService;
import com.yzx.party.vo.QueryCS;
import com.yzx.party.vo.QueryLog;
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
import java.util.Arrays;

/**
 * @author yzx
 * @version JDK 1.8.0_131
 * @since 2022/2/16 15:59
 */
//日志管理控制器
@Controller
//这个是全局都会生效的路径
@RequestMapping("/admin/superAdmin")
public class LogController {

    @Autowired
    LogService logService;
    @Autowired
    CollegeService collegeService;
    @Autowired
    DeleteLogBySizeMapper deleteLogBySizeMapper;

    //展示所有的评论
    @GetMapping("/logs")
    public String logs(@PageableDefault(size = 6,sort = {"operationtime"},direction = Sort.Direction.DESC) Pageable pageable,
                       Model model, QueryLog queryLog){
        model.addAttribute("listColleges",collegeService.listColleges());
        model.addAttribute("page",logService.listLogs(pageable, queryLog));
        return "superadmin/logshow";
    }

    //展示所有的日志
    @PostMapping("/logs/search")
    public String search(@PageableDefault(size = 6,sort = {"operationtime"},direction = Sort.Direction.DESC) Pageable pageable,
                           Model model, QueryLog queryLog){
        model.addAttribute("listColleges",collegeService.listColleges());
        model.addAttribute("page",logService.listLogs(pageable, queryLog));
        return "superadmin/logshow :: logSearchList";
    }


    //删除单条日志
    @GetMapping("/deleteLog/{id}")
    public String deleteComment(@PathVariable Integer id, RedirectAttributes attributes){
        logService.deleteLog(id);
        attributes.addFlashAttribute("message","删除单条日志成功");
        return "redirect:/admin/superAdmin/logs";
    }


    //批量删除，参数为前台传递过来的以“,”分割的id字符串
    @GetMapping("/deleteSomeLog/{checkedID}")
    public String deleteSome(@PathVariable String checkedID,RedirectAttributes attributes){
        //切分id，并且逐条删除
        String[] splitId = checkedID.split(",");
        for (String id:splitId){
            logService.deleteLog(Integer.parseInt(id));
        }
        attributes.addFlashAttribute("message","批量删除"+splitId.length+"条日志成功");
        return "redirect:/admin/superAdmin/logs";
    }


    //指定删除多少条日志
    @GetMapping("/deleteLogBySize/{size}")
    public String deleteLogBySize(@PathVariable Integer size,RedirectAttributes attributes){
        deleteLogBySizeMapper.deleteLogBySize(size);
        attributes.addFlashAttribute("message","批量删除"+size+"条日志成功");
        return "redirect:/admin/superAdmin/logs";
    }


    //删除全部日志
    @GetMapping("/deleteAllLog")
    public String deleteAllLog(RedirectAttributes attributes){
        deleteLogBySizeMapper.deleteAllLog();
        attributes.addFlashAttribute("message","删除全部日志成功");
        return "redirect:/admin/superAdmin/logs";
    }




}
