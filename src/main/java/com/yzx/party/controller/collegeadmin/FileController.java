package com.yzx.party.controller.collegeadmin;

import com.alibaba.fastjson.JSON;
import com.yzx.party.aspect.OperationLogAnnotation;
import com.yzx.party.mapper.NewsChartMapper;
import com.yzx.party.pojo.Admins;
import com.yzx.party.pojo.MyFiles;
import com.yzx.party.pojo.News;
import com.yzx.party.service.*;
import com.yzx.party.util.IntroduceUtil;
import com.yzx.party.vo.NewsChart;
import com.yzx.party.vo.QueryFile;
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

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author yzx
 * @version JDK 1.8.0_131
 * @since 2022/1/28 11:16
 */
//文件管理控制器
@Controller
//这个是全局都会生效的路径
@RequestMapping("/admin/collegeAdmin")
public class FileController {

    @Autowired
    PersonService personService;
    @Autowired
    CollegeService collegeService;
    @Autowired
    MajorService majorService;
    @Autowired
    MyFilesService filesService;

    //前往文件管理
    @GetMapping("/files")
    public String files(@PageableDefault(size = 6,sort = {"uploadtime"},direction = Sort.Direction.DESC) Pageable pageable,
                        QueryFile queryFile,Model model, HttpSession session){
        //获取登录的管理员
        Admins loginAdmin = (Admins)session.getAttribute("admin");
        //默认只能看见学校办公室
        Integer college_id = 1;
        //获取管理员所在的学院
        if (loginAdmin!=null){
            college_id = loginAdmin.getCollege_id();
        }
        model.addAttribute("listMajors",majorService.listMajors(college_id));
        //暂时只需要筛选出对应学院的文件即可
        queryFile.setCollege_id(college_id);
        model.addAttribute("page",filesService.listFiles(pageable,queryFile));
        return "collegeadmin/fileshow";
    }


    //前往文件管理
    @PostMapping("/files/search")
    @OperationLogAnnotation(operaType = "搜索",operaDesc = "搜索支部文件")
    public String search(@PageableDefault(size = 6,sort = {"uploadtime"},direction = Sort.Direction.DESC) Pageable pageable,
                        QueryFile queryFile,Model model, HttpSession session){
        //获取登录的管理员
        Admins loginAdmin = (Admins)session.getAttribute("admin");
        //默认只能看见学校办公室
        Integer college_id = 1;
        //获取管理员所在的学院
        if (loginAdmin!=null){
            college_id = loginAdmin.getCollege_id();
        }
        model.addAttribute("listMajors",majorService.listMajors(college_id));
        //暂时只需要筛选出对应学院的文件即可
        queryFile.setCollege_id(college_id);
        model.addAttribute("page",filesService.listFiles(pageable,queryFile));
        return "collegeadmin/fileshow :: fileSearchList";
    }

//    //文件的下载
    @GetMapping("/download/{id}")
    @OperationLogAnnotation(operaType = "下载",operaDesc = "下载支部党员文件")
    public void downloadFile(@PathVariable Integer id,HttpServletResponse response) throws UnsupportedEncodingException {
        MyFiles fileById = filesService.getFileById(id);
        String fileName = fileById.getTitle();
        if (fileName != null) {
            //找到文件存储路径
            String pathName = "./filePersonDir/"+fileName;
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



    //文件的删除
    @GetMapping("/deleteFile/{id}")
    @OperationLogAnnotation(operaType = "删除",operaDesc = "删除支部文件")
    public String deleteFile(@PathVariable Integer id,RedirectAttributes attributes){
        MyFiles f = filesService.getFileById(id);
        //先从数据库中删除数据
        filesService.deleteFiles(id);
        //设置文件路径
        String pathName = "./filedir/"+f.getTitle();
        //获取文件
        File file = new File(pathName);
        //删除文件
        if (file.exists()) {
            file.delete();
            attributes.addFlashAttribute("message","文件删除成功");
        }else {
            attributes.addFlashAttribute("message","文件删除失败");
        }
        return "redirect:/admin/collegeAdmin/files";
    }





}
