package com.yzx.party.controller.collegeadmin;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import com.yzx.party.aspect.OperationLogAnnotation;
import com.yzx.party.pojo.Admins;
import com.yzx.party.pojo.Colleges;
import com.yzx.party.pojo.Majors;
import com.yzx.party.pojo.Persons;
import com.yzx.party.service.CollegeService;
import com.yzx.party.service.MajorService;
import com.yzx.party.service.PersonService;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author yzx
 * @version JDK 1.8.0_131
 * @since 2022/1/21 16:00
 */
//用于导出数据
//学院的控制器
@Controller
//这个是全局都会生效的路径
@RequestMapping("/admin/collegeAdmin")
public class ExcelController {

    @Autowired
    PersonService personService;
    @Autowired
    MajorService majorService;
    @Autowired
    CollegeService collegeService;

    //前往信息下载
    @GetMapping("/toInfoDownload")
    public String download(HttpSession session, Model model){
        //获取登录的管理员
        Admins loginAdmin = (Admins)session.getAttribute("admin");
        //默认只能看见学校办公室
        Integer college_id = 1;
        //获取管理员所在的学院
        if (loginAdmin!=null){
            college_id = loginAdmin.getCollege_id();
        }
        List<Persons> personsList = personService.listPersons(college_id);
        List<Majors> majorsList = majorService.listMajors(college_id);
        model.addAttribute("personsList",personsList);
        model.addAttribute("majorsList",majorsList);
        return "collegeadmin/infodownload";
    }



    //信息下载
    private static final Logger log = LoggerFactory.getLogger(ExcelController.class);
    @GetMapping("/exportExcel")
    @OperationLogAnnotation(operaType = "下载",operaDesc = "下载支部党员信息")
    public void exportExcel( HttpServletResponse response,HttpSession session) {
        //获取登录的管理员
        Admins loginAdmin = (Admins)session.getAttribute("admin");
        //默认只能看见学校办公室
        Integer college_id = 1;
        //获取管理员所在的学院
        if (loginAdmin!=null){
            college_id = loginAdmin.getCollege_id();
        }
        //==========================预处理数据=========================================
        //获取学院的名字来构建excel表的名字
        String collegeName = collegeService.getCollegeById(college_id).getName();
        //获得所有的人员信息
        List<Persons> personsList = personService.listPersons(college_id);
        //为了防止修改数据库中的信息，重新复制一份信息集合
        List<Persons> personsChangeList = new ArrayList<>();
        for (Persons databasePerson:personsList){
            Persons changePerson = new Persons();
            //将遍历的人员信息赋值给新的人员信息
            BeanUtils.copyProperties(databasePerson,changePerson);
            if ("officialM".equals(changePerson.getAppearance())){
                changePerson.setAppearance("中共党员");
            }else if ("prepareM".equals(changePerson.getAppearance())){
                changePerson.setAppearance("预备党员");
            }else {
                changePerson.setAppearance("积极分子");
            }
            personsChangeList.add(changePerson);
        }
        //==========================预处理数据结束=========================================
        try {
            // 设置响应输出的头类型及下载文件的默认名称
            String fileName = new String((collegeName+"人员信息表.xls").getBytes("utf-8"), "ISO-8859-1");
            response.addHeader("Content-Disposition", "attachment;filename=" + fileName);
            response.setContentType("application/vnd.ms-excel;charset=gb2312");
            //导出，这里的第一个参数是固定的写法，第二个参数为导出的类对象，第三个参数为导出的集合
            Workbook workbook = ExcelExportUtil.exportExcel(new ExportParams(), Persons.class, personsChangeList);
            workbook.write(response.getOutputStream());
            log.info("请求 exportExcel end ......");
        } catch (IOException e) {
            log.info("请求 exportExcel 异常：{}", e.getMessage());
        }
    }


}




