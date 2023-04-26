package com.yzx.party.controller.collegeadmin;

import com.sun.org.apache.xpath.internal.operations.Mod;
import com.yzx.party.aspect.OperationLogAnnotation;
import com.yzx.party.myException.NotFoundException;
import com.yzx.party.pojo.Admins;
import com.yzx.party.pojo.News;
import com.yzx.party.pojo.TaskFile;
import com.yzx.party.pojo.Tasks;
import com.yzx.party.service.TaskFileService;
import com.yzx.party.service.TaskService;
import com.yzx.party.util.MarkdownUtil;
import com.yzx.party.util.TaskUtil;
import com.yzx.party.vo.QueryMeeting;
import com.yzx.party.vo.QueryTask;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import java.io.File;
import java.util.*;

/**
 * @author yzx
 * @version JDK 1.8.0_131
 * @since 2022/3/12 10:04
 */
//任务提交控制器
@Controller
//这个是全局都会生效的路径
@RequestMapping("/admin/collegeAdmin")
public class TaskFileController {

    @Autowired
    TaskService taskService;
    @Autowired
    TaskFileService taskFileService;

    //查看任务
    @GetMapping("/tasks")
    public String tasks(@PageableDefault(size = 100,sort = {"publictime"},direction = Sort.Direction.DESC) Pageable pageable,
                        Model model, QueryTask queryTask,HttpSession session){
        //获得登录者和其所在管理员
        Admins loginAdmin = (Admins)session.getAttribute("admin");
        Integer college_id = loginAdmin.getCollege_id();
        //先查询出所有的任务
        List<Tasks> tasksList = taskService.listTasks(pageable, queryTask).getContent();
        //获得未完成和已完成任务
        List<List<Tasks>> resultList = new TaskUtil().TaskUtilHelper(tasksList, college_id);
        //传递任务，第一个元素未完成，第二个元素已完成
        model.addAttribute("finishedTask",resultList.get(1));
        model.addAttribute("unFinishedTask",resultList.get(0));
        //为了防止空指针异常，传递一个空的对象
        model.addAttribute("formatTask",new Tasks());
        return "collegeadmin/taskshow";
    }


    //查看详情
    @GetMapping("/task/{id}")
    public String taskAbout(@PageableDefault(size = 100,sort = {"publictime"},direction = Sort.Direction.DESC) Pageable pageable,
                            @PathVariable Integer id, QueryTask queryTask,Model model,HttpSession session){
        Tasks taskById = taskService.getTaskById(id);
        if (taskById==null){
            throw new NotFoundException("该任务不存在");
        }
        //================markdown转化为html开始=====================//
        //用于格式转化之后的输出
        Tasks t = new Tasks();
        //将对象taskById复制给t，那么后面的属性修改就不会修改数据库中的值
        BeanUtils.copyProperties(taskById,t);
        String content = t.getContent();
        t.setContent(new MarkdownUtil().markdownToHtmlExtensions(content));
        model.addAttribute("formatTask",t);
        //================markdown转化为html结束=====================//
        //携带回去选中的模块id
        model.addAttribute("activeId",id);
        //获得登录者和其所在管理员
        Admins loginAdmin = (Admins)session.getAttribute("admin");
        Integer college_id = loginAdmin.getCollege_id();
        //先查询出所有的任务
        List<Tasks> tasksList = taskService.listTasks(pageable, queryTask).getContent();
        //获得未完成和已完成任务
        List<List<Tasks>> resultList = new TaskUtil().TaskUtilHelper(tasksList, college_id);
        //传递任务，第一个元素未完成，第二个元素已完成
        model.addAttribute("finishedTask",resultList.get(1));
        model.addAttribute("unFinishedTask",resultList.get(0));
        return "collegeadmin/taskshow";
    }


    //完成情况文件上传
    @PostMapping("/taskUploadFile")
    @OperationLogAnnotation(operaType = "新增",operaDesc = "任务完成情况汇报")
    public String taskUploadFile(@RequestParam("file") MultipartFile file,
                                 @RequestParam("task_id") Integer task_id,
                                 @RequestParam("title") String taskTitle,
                                 @RequestParam("content") String content,
                                 RedirectAttributes attributes,
                                 HttpSession session){
        //获得登录者和其所在管理员
        Admins loginAdmin = (Admins)session.getAttribute("admin");
        Integer college_id = loginAdmin.getCollege_id();
        TaskFile taskFile = new TaskFile();
        //如果文件存在才存储
        if (!file.isEmpty()){
            // 获取文件名
            String fileName = file.getOriginalFilename();
            //如果存在同名文件，那么不提交
            if (taskFileService.getTaskFileByFileName(fileName)!=null){
                attributes.addFlashAttribute("errorMessage","该文件名已经存在");
                return "redirect:/admin/collegeAdmin/task/"+task_id;
            }else {
                //不存在同名文件，则上传文件
                try{
                    // 设置文件存储路径
                    String filePath = "./fileTaskDir/";
                    String path = filePath + fileName;
                    File dest = new File(new File(path).getAbsolutePath());
                    // 检测是否存在目录
                    //因此这里使用.getParentFile()，目的就是取文件前面目录的路径
                    if (!dest.getParentFile().exists()) {
                        // 新建文件夹
                        dest.getParentFile().mkdirs();
                    }
                    file.transferTo(dest);// 文件写入
                    //数据库设置文件名
                    taskFile.setFile(fileName);
                }catch (Exception e) {
                    e.printStackTrace();
                    attributes.addFlashAttribute("errorMessage","提交失败");
                }
            }
        }

        //保存信息到数据库
        taskFile.setAdmin_id(loginAdmin.getId());
        taskFile.setCollege_id(loginAdmin.getCollege_id());
        taskFile.setComptime(new Date());
        taskFile.setTitle(taskTitle);
        taskFile.setTask_id(task_id);
        taskFile.setContent(content);
        TaskFile t = taskFileService.saveTaskFile(taskFile);

        //因为已经保存了数据，所以判定为任务已经完成
        Tasks taskById = taskService.getTaskById(task_id);
        //先获得原先完成的学院数据
        String compcollege_id = taskById.getCompcollege_id();
        //拼接字符串
        taskById.setCompcollege_id(compcollege_id+","+college_id);
        //更新任务的完成情况
        taskService.updateTasks(task_id, taskById);

        if (t==null){
            attributes.addFlashAttribute("errorMessage","提交失败");
        }else {
            attributes.addFlashAttribute("message","提交成功");
        }

        return "redirect:/admin/collegeAdmin/task/"+task_id;
    }





}
