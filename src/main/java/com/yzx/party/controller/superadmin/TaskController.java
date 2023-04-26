package com.yzx.party.controller.superadmin;

import com.yzx.party.aspect.OperationLogAnnotation;
import com.yzx.party.pojo.*;
import com.yzx.party.service.CollegeService;
import com.yzx.party.service.TaskFileService;
import com.yzx.party.service.TaskService;
import com.yzx.party.vo.QueryCS;
import com.yzx.party.vo.QueryTask;
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

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.net.URLEncoder;

/**
 * @author yzx
 * @version JDK 1.8.0_131
 * @since 2022/3/11 11:12
 */
//任务管理制器
@Controller
//这个是全局都会生效的路径
@RequestMapping("/admin/superAdmin")
public class TaskController {
    @Autowired
    TaskService taskService;
    @Autowired
    TaskFileService taskFileService;
    @Autowired
    CollegeService collegeService;

    //展示所有的任务
    @GetMapping("/tasks")
    public String tasks(@PageableDefault(size = 6,sort = {"publictime"},direction = Sort.Direction.DESC) Pageable pageable,
                        Model model, QueryTask queryTask){
        //查询所有任务
        model.addAttribute("listColleges",collegeService.listColleges());
        model.addAttribute("page",taskService.listTasks(pageable, queryTask));
        return "superadmin/task";
    }

    //搜索任务
    @PostMapping("/taskSearch")
    @OperationLogAnnotation(operaType = "搜索",operaDesc = "搜索任务")
    public String taskSearch(@PageableDefault(size = 6,sort = {"publictime"},direction = Sort.Direction.DESC) Pageable pageable,
                        Model model, QueryTask queryTask ){
        //查询所有任务
        model.addAttribute("listColleges",collegeService.listColleges());
        model.addAttribute("page",taskService.listTasks(pageable, queryTask));
        return "superadmin/task :: taskList";
    }




    //前往发布任务
    @GetMapping("/taskInput")
    public String toTaskInput(Model model){
        //为了防止和修改的时候产生的空指针异常，先传一个空的对象过去
        model.addAttribute("getTaskById",new Tasks());
        return "superadmin/taskinput";
    }

    //新增
    @PostMapping("/taskInput")
    @OperationLogAnnotation(operaType = "新增",operaDesc = "发布任务")
    public String taskInput(Tasks tasks,RedirectAttributes attributes,Model model,HttpSession session){
        //获得登录者
        Admins loginAdmin = (Admins)session.getAttribute("admin");
        //设置提醒的标识为0
        tasks.setRemind(0);
        //设置发布者
        tasks.setAdmin_id(loginAdmin.getId());
        tasks.setCollege_id(loginAdmin.getCollege_id());
        Tasks t = taskService.saveTask(tasks);
        if (t!=null){
            //新增成功
            attributes.addFlashAttribute("message","发布任务成功");
        }else {
            //新增失败
            attributes.addFlashAttribute("message","发布任务失败");
        }
        return "redirect:/admin/superAdmin/tasks";
    }


    //前往修改
    @GetMapping("/taskUpdate/{id}")
    public String toTaskUpdate(@PathVariable Integer id, Model model){
        model.addAttribute("getTaskById",taskService.getTaskById(id));
        return "superadmin/taskinput";
    }

    //修改
    @PostMapping("/taskUpdate/{id}")
    @OperationLogAnnotation(operaType = "修改",operaDesc = "修改任务")
    public String taskUpdate(@PathVariable Integer id,Tasks tasks,RedirectAttributes attributes,HttpSession session){
        Tasks taskById = taskService.getTaskById(id);
        //获得登录者
        Admins loginAdmin = (Admins)session.getAttribute("admin");
        //先获得原来的提醒次数并且不加修改
        tasks.setRemind(taskById.getRemind());
        //获得原来的已完成学院id并且不修改
        tasks.setCompcollege_id(taskById.getCompcollege_id());
        //设置发布者
        tasks.setAdmin_id(loginAdmin.getId());
        tasks.setCollege_id(loginAdmin.getCollege_id());
        Tasks t = taskService.updateTasks(id,tasks);
        if (t!=null){
            //新增成功
            attributes.addFlashAttribute("message","发布任务成功");
        }else {
            //新增失败
            attributes.addFlashAttribute("message","发布任务失败");
        }
        return "redirect:/admin/superAdmin/tasks";
    }


    //删除任务
    @GetMapping("/taskDelete/{id}")
    @OperationLogAnnotation(operaType = "删除",operaDesc = "删除单个任务")
    public String taskDelete(@PathVariable Integer id, RedirectAttributes attributes){
        System.out.println(taskService.getTaskById(id).getId());
        System.out.println(taskService.getTaskById(id).getTitle());
        taskService.deleteTask(id);
        attributes.addFlashAttribute("message","删除任务成功");
        return "redirect:/admin/superAdmin/tasks";
    }

    //删除多个任务
    @GetMapping("/taskDeleteSome/{checkedID}")
    @OperationLogAnnotation(operaType = "删除",operaDesc = "批量删除任务")
    public String taskDeleteSome(@PathVariable String checkedID, RedirectAttributes attributes){
        String[] splitId = checkedID.split(",");
        for (String id:splitId){
            taskService.deleteTask(Integer.parseInt(id));
        }
        attributes.addFlashAttribute("message","删除"+splitId.length+"个任务成功");
        return "redirect:/admin/superAdmin/tasks";
    }


    //展示所有任务完成情况
    @GetMapping("/tasksComp")
    public String tasksComp(@PageableDefault(size = 6,sort = {"comptime"},direction = Sort.Direction.DESC) Pageable pageable,
                            Model model, QueryTask queryTask){
        //查询所有任务
        model.addAttribute("listColleges",collegeService.listColleges());
        model.addAttribute("page",taskFileService.listTaskFiles(pageable, queryTask));
        return "superadmin/taskcomp";
    }

    //查询所有的完成情况
    @PostMapping("/tasksCompSearch")
    public String tasksCompSearch(@PageableDefault(size = 6,sort = {"comptime"},direction = Sort.Direction.DESC) Pageable pageable,
                                  Model model, QueryTask queryTask){
        //查询所有任务
        model.addAttribute("listColleges",collegeService.listColleges());
        model.addAttribute("page",taskFileService.listTaskFiles(pageable, queryTask));
        return "superadmin/taskcomp :: taskCompList";
    }


    //下载单个文件
    @GetMapping("/downloadTaskFile/{id}")
    @OperationLogAnnotation(operaType = "下载",operaDesc = "下载任务附件")
    public void downloadFile(@PathVariable Integer id, HttpServletResponse response) throws UnsupportedEncodingException {
        TaskFile fileById = taskFileService.getTaskFileById(id);
        String fileName = fileById.getFile();
        if (fileName != null) {
            //找到文件存储路径
            String pathName = "./fileTaskDir/"+fileName;
            //获取文件
            File file = new File(pathName);

            if (file.exists()) {
                //设置编码
                response.setCharacterEncoding("UTF-8");
                // 设置文件名
                response.setHeader("Content-Disposition", "attachment;filename="+  fileName +";filename*=utf-8''"+ URLEncoder.encode(fileName,"UTF-8"));
                // 告知浏览器文件的大小
                response.addHeader("Content-Length", "" + file.length());
                // 设置强制下载不打开
                response.setContentType("application/force-download");
                byte[] buffer = new byte[1024];
                FileInputStream fis = null;
                BufferedInputStream bis = null;
                try {
                    fis = new FileInputStream(file);
                    bis = new BufferedInputStream(fis);
                    OutputStream os = response.getOutputStream();
                    int i = bis.read(buffer);
                    while (i != -1) {
                        os.write(buffer, 0, i);
                        i = bis.read(buffer);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally { // 做关闭操作
                    if (bis != null) {
                        try {
                            bis.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    if (fis != null) {
                        try {
                            fis.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
    }


    //删除任务以及文件
    @GetMapping("/deleteTaskFile/{id}")
    @OperationLogAnnotation(operaType = "删除",operaDesc = "删除任务附件")
    public String deleteFile(@PathVariable Integer id,RedirectAttributes attributes){
        //获得任务文件
        TaskFile f = taskFileService.getTaskFileById(id);
        //获得任务所在学院id
        Integer college_id = f.getCollege_id();
        //获得任务文件对应的任务
        Integer task_id = f.getTask_id();
        //获得文件的名字
        String fileName = f.getFile();
        if (fileName!=null){
            //设置文件路径
            String pathName = "./fileTaskDir/"+f.getFile();
            //获取文件
            File file = new File(pathName);
            //删除文件
            if (file.exists()) {
                //删除文件
                file.delete();
            }else {
                attributes.addFlashAttribute("message","删除失败");
            }
        }

        //删除数据库中数据
        taskFileService.deleteTaskFile(id);
        //将完成情况替换一个学院管理员所在的学院id为空
        //获得任务
        Tasks t = taskService.getTaskById(task_id);
        String compcollege_id = t.getCompcollege_id();
        String resultCompcollege_id = compcollege_id.replaceFirst(String.valueOf(college_id), "");
        t.setCompcollege_id(resultCompcollege_id);
        taskService.updateTasks(task_id,t);
        attributes.addFlashAttribute("message","删除成功");
        return "redirect:/admin/superAdmin/tasksComp";
    }





}
