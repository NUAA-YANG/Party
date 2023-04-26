package com.yzx.party.controller.personview;

import com.yzx.party.controller.collegeadmin.ExcelController;
import com.yzx.party.pojo.Admins;
import com.yzx.party.pojo.MyFiles;
import com.yzx.party.pojo.Persons;
import com.yzx.party.service.CollegeService;
import com.yzx.party.service.MajorService;
import com.yzx.party.service.MyFilesService;
import com.yzx.party.service.PersonService;
import com.yzx.party.util.MD5Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.util.Date;

/**
 * @author yzx
 * @version JDK 1.8.0_131
 * @since 2022/1/22 11:00
 */
//个人信息控制器
@Controller
//这个是全局都会生效的路径
@RequestMapping("/personShow")
public class PPersonalInfoController {

    @Autowired
    PersonService personService;
    @Autowired
    CollegeService collegeService;
    @Autowired
    MajorService majorService;
    @Autowired
    MyFilesService filesService;


    //前往用户信息模块
    @GetMapping("/personalInfo")
    public String showInfo(HttpSession session, Model model){
        //获取登录的人员
        Persons loginPerson = (Persons)session.getAttribute("loginPerson");
        String appearance =  loginPerson.getAppearance();
        if ("officialM".equals(appearance)){
            appearance = "中共党员";
        }else if ("prepareM".equals(appearance)){
            appearance = "预备党员";
        }else {
            appearance = "积极分子";
        }
        //直接携带政治面貌
        model.addAttribute("appearance",appearance);
        //直接携带学院名字
        model.addAttribute("collegeName",collegeService.getCollegeById(loginPerson.getCollege_id()).getName());
        model.addAttribute("loginPerson",personService.getPersonsById(loginPerson.getId()));
        //直接携带专业名字
        model.addAttribute("majorName",majorService.getMajorById(loginPerson.getMajor_id()).getName());
        return "personalinfo";
    }



    //前往更改用户信息
    @GetMapping("/personalInfoInput/{id}")
    public String infoInput(@PathVariable Integer id, HttpSession session, Model model){
        //获取登录的人员
        Persons loginPerson = (Persons)session.getAttribute("loginPerson");
        model.addAttribute("loginPerson",personService.getPersonsById(loginPerson.getId()));
        model.addAttribute("majorList",majorService.listMajors(loginPerson.getCollege_id()));
        return "personinfoinput";
    }




    //用户信息更改
    @PostMapping("/personalInfoInput/{id}")
    public String infoUpdate(@PathVariable Integer id,@RequestParam String originalpassword, RedirectAttributes attributes, Persons persons,Model model){
        MD5Util md5Util = new MD5Util();
        //数据库中获取的人员
        Persons dataPerson = personService.getPersonsById(id);
        //原始密码
        String password = dataPerson.getPassword();
        //如果输入的原始密码和数据库中不匹配，那么判定错误，不可修改信息
        if (!password.equals(md5Util.md5(originalpassword)) ){
            model.addAttribute("loginPerson",personService.getPersonsById(dataPerson.getId()));
            model.addAttribute("majorList",majorService.listMajors(dataPerson.getCollege_id()));
            model.addAttribute("errorMessage","原始密码错误，不可修改信息!");
            return "personinfoinput";
        }else {
            //如果输入新的字符串(不为空)，就是重置密码，那么就封装新的密码
            if (!"".equals(persons.getPassword())){
                //获得新密码
                String newPassword = persons.getPassword();
                //使用封装的密码
                persons.setPassword(md5Util.md5(newPassword));
            }else {
                //如果没有输入字符，那么就使用原来的密码
                persons.setPassword(password);
            }
        }
        //设置学院不变
        persons.setCollege_id(dataPerson.getCollege_id());
        persons.setAppearance(dataPerson.getAppearance());
        Persons p = personService.updatePersons(id, persons);
        if (p!=null){
            //修改成功
            attributes.addFlashAttribute("message","修改信息成功");
        }else {
            //修改失败
            attributes.addFlashAttribute("message","修改信息失败");
        }
        return "redirect:/personShow/personalInfo";
    }


    //前往文件上传模块
    @GetMapping("/toUploadFile")
    public String toUploadFile(){
        return "uploadfile";
    }


    private static final Logger log = LoggerFactory.getLogger(PPersonalInfoController.class);
    //文件的上传
    @PostMapping("/uploadFile")
    public String uploadFile(@RequestParam("file") MultipartFile file,
                             RedirectAttributes attributes,HttpSession session){
        try{
            // 获取文件名
            String fileName = file.getOriginalFilename();
            // 设置文件存储路径
            String filePath = "./filePersonDir/";
            String path = filePath + fileName;
            File dest = new File(new File(path).getAbsolutePath());// dist为文件，有多级目录的文件
            // 检测是否存在目录
            if (!dest.getParentFile().exists()) {//因此这里使用.getParentFile()，目的就是取文件前面目录的路径
                dest.getParentFile().mkdirs();// 新建文件夹
            }
            file.transferTo(dest);// 文件写入

            //保存到数据库
            Persons loginPerson = (Persons)session.getAttribute("loginPerson");
            MyFiles myFiles = new MyFiles();
            myFiles.setPerson_id(loginPerson.getId());
            myFiles.setCollege_id(loginPerson.getCollege_id());
            myFiles.setTitle(fileName);
            myFiles.setUploadtime(new Date());
            myFiles.setPerson_name(loginPerson.getName());
            myFiles.setMajor_id(loginPerson.getMajor_id());
            //如果存在该名字的文件，那么不存储
            if (filesService.getFileByTitle(fileName)!=null){
                attributes.addFlashAttribute("errorMessage","该文件名已经存在");
            }else {
                attributes.addFlashAttribute("message","上传文件成功");
                filesService.saveFiles(myFiles);
            }
        } catch (Exception e) {
            e.printStackTrace();
            attributes.addFlashAttribute("errorMessage","上传文件失败");
        }
        return "redirect:/personShow/toUploadFile";
    }



}
