package com.yzx.party.controller.superadmin;

import com.yzx.party.aspect.OperationLogAnnotation;
import com.yzx.party.pojo.Colleges;
import com.yzx.party.service.CollegeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.List;

/**
 * @author yzx
 * @version JDK 1.8.0_131
 * @since 2022/1/7 10:39
 */
//学院的控制器
@Controller
//这个是全局都会生效的路径
@RequestMapping("/admin/superAdmin")
public class CollegesController {
    @Autowired
    CollegeService collegeService;

    //学院展示
    @GetMapping("/colleges")
    public String colleges(@PageableDefault(size = 5,sort = {"id"},direction = Sort.Direction.ASC) Pageable pageable,
                           Model model , Integer college_id){
        //用于筛选学院
        model.addAttribute("listColleges",collegeService.listColleges());
        model.addAttribute("page",collegeService.listColleges(pageable, college_id));
        return "superadmin/college";
    }

    //后端的筛选
    @PostMapping("/college/search")
    @OperationLogAnnotation(operaType = "搜索",operaDesc = "搜索学院")
    public String search(@PageableDefault(size = 5,sort = {"id"},direction = Sort.Direction.ASC) Pageable pageable,
                           Model model , Integer college_id){
        //用于筛选学院
        model.addAttribute("listColleges",collegeService.listColleges());
        model.addAttribute("page",collegeService.listColleges(pageable, college_id));
        return "superadmin/college :: collegeList";
    }


    //前往新增
    @GetMapping("/collegesInput")
    public String toCollegesInput(Model model){
        //为了防止和修改的时候产生的空指针异常，先传一个空的对象过去
        model.addAttribute("colleges",new Colleges());
        return "superadmin/collegeinput";
    }

    //新增
    @PostMapping("/colleges")
    @OperationLogAnnotation(operaType = "新增",operaDesc = "新增学院")
    /*@Valid 表示需要校验对象,BindResult是接受校验之后的结果*/
    public String collegesInput(@Valid Colleges colleges, BindingResult result, RedirectAttributes attributes){
        //name表示colleges中需要校验的属性，属性名必须完全一样，"nameError"可写可不写  校验是否重复
        if (collegeService.getCollegeByName(colleges.getName())!=null){
            System.out.println(collegeService.getCollegeByName(colleges.getName()));
            result.rejectValue("name","nameError","该学院党支部已经存在");
        }
        //校验失败重新返回
        if (result.hasErrors()){
            return "superadmin/collegeinput";
        }
        Colleges c = collegeService.saveColleges(colleges);
        if (c!=null){
            //新增成功
            attributes.addFlashAttribute("message","新增学院党支部成功");
        }else {
            //新增失败
            attributes.addFlashAttribute("message","新增学院党支部失败");
        }
        return "redirect:/admin/superAdmin/colleges";
    }


    //前往修改
    @GetMapping("/collegesUpdate/{id}")
    public String toCollegeUpdate(@PathVariable Integer id,Model model){
        model.addAttribute("colleges",collegeService.getCollegeById(id));
        return "superadmin/collegeinput";
    }

    //修改
    @PostMapping("/collegesUpdate/{id}")
    @OperationLogAnnotation(operaType = "修改",operaDesc = "修改学院信息")
    public String collegeUpdate(@Valid Colleges colleges, BindingResult result,@PathVariable Integer id,RedirectAttributes attributes){
        Colleges databaseCollege = collegeService.getCollegeByNameAndCreatePartyTime(colleges.getName(),colleges.getCreatepartytime());
        //再次校验重复 不同于新增，修改的时候如果有这个对象并且时间也一样才是真的存在
        if ((databaseCollege!=null)){
            //name表示colleges中需要校验的属性，属性名必须完全一样，"nameError"可写可不写  校验是否重复
            result.rejectValue("name","nameError","该学院党支部已经存在");
        }
        //校验失败重新返回
        if (result.hasErrors()){
            return "superadmin/collegeinput";
        }
        Colleges c = collegeService.updateColleges(id, colleges);
        if (c!=null){
            //新增成功
            attributes.addFlashAttribute("message","修改学院党支部成功");
        }else {
            //新增失败
            attributes.addFlashAttribute("message","修改学院党支部失败");
        }
        return "redirect:/admin/superAdmin/colleges";
    }


    @GetMapping("/collegesDelete/{id}")
    @OperationLogAnnotation(operaType = "删除",operaDesc = "删除学院")
    public String collegeDelete(@PathVariable Integer id,RedirectAttributes attributes){
        collegeService.deleteColleges(id);
        attributes.addFlashAttribute("message","删除学院成功");
        return "redirect:/admin/superAdmin/colleges";
    }








}
