package com.yzx.party.controller.collegeadmin;

import com.yzx.party.aspect.OperationLogAnnotation;
import com.yzx.party.pojo.Admins;
import com.yzx.party.pojo.Colleges;
import com.yzx.party.pojo.Majors;
import com.yzx.party.service.CollegeService;
import com.yzx.party.service.MajorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

/**
 * @author yzx
 * @version JDK 1.8.0_131
 * @since 2022/1/7 10:39
 */
//学院的控制器
@Controller
//这个是全局都会生效的路径
@RequestMapping("/admin/collegeAdmin")
public class MajorsController {
    @Autowired
    MajorService majorService;

    //专业展示
    @GetMapping("/majors")
    public String majors(@PageableDefault(size = 5,sort = {"id"},direction = Sort.Direction.ASC) Pageable pageable,
                           Model model , Integer major_id,HttpSession session){
        //获取登录的管理员
        Admins loginAdmin = (Admins)session.getAttribute("admin");
        //默认只能看见学校办公室
        Integer college_id = 1;
        //获取管理员所在的学院
        if (loginAdmin!=null){
            college_id = loginAdmin.getCollege_id();
        }
        //用于筛选专业
        model.addAttribute("listMajors",majorService.listMajors(college_id));
        model.addAttribute("page",majorService.listMajors(pageable, major_id,college_id));
        return "collegeadmin/major";
    }

    //后端的筛选
    @PostMapping("/major/search")
    @OperationLogAnnotation(operaType = "搜索",operaDesc = "搜索学院专业党支部")
    public String search(@PageableDefault(size = 5,sort = {"id"},direction = Sort.Direction.ASC) Pageable pageable,
                           Model model , Integer major_id,HttpSession session){
        //获取登录的管理员
        Admins loginAdmin = (Admins)session.getAttribute("admin");
        //默认只能看见学校办公室
        Integer college_id = 1;
        //获取管理员所在的学院
        if (loginAdmin!=null){
            college_id = loginAdmin.getCollege_id();
        }
        //用于筛选专业
        model.addAttribute("listMajors",majorService.listMajors(college_id));
        model.addAttribute("page",majorService.listMajors(pageable, major_id,college_id));
        return "collegeadmin/major :: majorList";
    }


    //前往新增
    @GetMapping("/majorsInput")
    public String toMajorsInput(Model model){
        //为了防止和修改的时候产生的空指针异常，先传一个空的对象过去
        model.addAttribute("majors",new Majors());
        return "collegeadmin/majorinput";
    }

    //新增
    @PostMapping("/majors")
    @OperationLogAnnotation(operaType = "新增",operaDesc = "新增学院专业党支部")
    /*@Valid 表示需要校验对象,BindResult是接受校验之后的结果*/
    public String majorsInput(@Valid Majors majors, BindingResult result, RedirectAttributes attributes, HttpSession session){
        //name表示colleges中需要校验的属性，属性名必须完全一样，"nameError"可写可不写  校验是否重复
        if (majorService.getMajorByName(majors.getName())!=null){
            result.rejectValue("name","nameError","该专业党支部已经存在");
        }
        //校验失败重新返回
        if (result.hasErrors()){
            return "collegeadmin/majorinput";
        }
        //新增的专业全部是在管理员所在的学院下面
        Admins loginAdmin = (Admins)session.getAttribute("admin");
        majors.setCollege_id(loginAdmin.getCollege_id());
        Majors m = majorService.saveMajors(majors);
        if (m!=null){
            //新增成功
            attributes.addFlashAttribute("message","新增专业党支部成功");
        }else {
            //新增失败
            attributes.addFlashAttribute("message","新增专业党支部失败");
        }
        return "redirect:/admin/collegeAdmin/majors";
    }


    //前往修改
    @GetMapping("/majorsUpdate/{id}")
    public String toMajorUpdate(@PathVariable Integer id,Model model){
        model.addAttribute("majors",majorService.getMajorById(id));
        return "collegeadmin/majorinput";
    }

    //修改
    @PostMapping("/majorsUpdate/{id}")
    @OperationLogAnnotation(operaType = "修改",operaDesc = "修改学院专业党支部")
    public String majorUpdate(@Valid Majors majors, BindingResult result,
                              @PathVariable Integer id,RedirectAttributes attributes,
                              HttpSession session){
        Majors databaseMajor = majorService.getMajorByNameAndCreatePartyTime(majors.getName(), majors.getCreatepartytime());
        //再次校验重复 不同于新增，修改的时候如果有这个对象并且时间也一样才是真的存在
        if ((databaseMajor!=null)){
            //name表示colleges中需要校验的属性，属性名必须完全一样，"nameError"可写可不写  校验是否重复
            result.rejectValue("name","nameError","该专业党支部已经存在");
        }
        //校验失败重新返回
        if (result.hasErrors()){
            return "collegeadmin/majorinput";
        }
        Admins loginAdmin = (Admins)session.getAttribute("admin");
        majors.setCollege_id(loginAdmin.getCollege_id());
        Majors m = majorService.updateMajors(id, majors);
        if (m!=null){
            //修改成功
            attributes.addFlashAttribute("message","修改专业党支部成功");
        }else {
            //修改失败
            attributes.addFlashAttribute("message","修改专业党支部失败");
        }
        return "redirect:/admin/collegeAdmin/majors";
    }


    @GetMapping("/majorsDelete/{id}")
    @OperationLogAnnotation(operaType = "删除",operaDesc = "删除学院党支部")
    public String collegeDelete(@PathVariable Integer id,RedirectAttributes attributes){
        majorService.deleteMajors(id);
        attributes.addFlashAttribute("message","删除专业成功");
        return "redirect:/admin/collegeAdmin/majors";
    }








}
