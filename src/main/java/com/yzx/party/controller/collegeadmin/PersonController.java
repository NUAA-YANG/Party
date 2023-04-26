package com.yzx.party.controller.collegeadmin;

import com.alibaba.fastjson.JSON;
import com.yzx.party.aspect.OperationLogAnnotation;
import com.yzx.party.pojo.Admins;
import com.yzx.party.pojo.Persons;
import com.yzx.party.service.MajorService;
import com.yzx.party.service.PersonService;

import com.yzx.party.util.MD5Util;
import com.yzx.party.vo.QueryPerson;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author yzx
 * @version JDK 1.8.0_131
 * @since 2022/1/12 9:38
 */
//后端的人员控制器
@Controller
//这个是全局都会生效的路径
@RequestMapping("/admin/collegeAdmin")
public class PersonController {
    @Autowired
    PersonService personService;
    @Autowired
    MajorService majorService;


    //后端的展示
    @GetMapping("/persons/{appearance}")
    public String Persons(@PageableDefault(size = 5,sort = {"id"},direction = Sort.Direction.DESC) Pageable pageable,
                           @PathVariable String appearance,Model model, QueryPerson queryPerson, HttpSession session){
        //获取登录的管理员
        Admins loginAdmin = (Admins)session.getAttribute("admin");
        //默认只能看见学校办公室
        Integer college_id = 1;
        //获取管理员所在的学院
        if (loginAdmin!=null){
            college_id = loginAdmin.getCollege_id();
        }
        //设置传递过来的政治面貌
        queryPerson.setAppearance(appearance);
        model.addAttribute("listMajor",majorService.listMajors(college_id));
        model.addAttribute("page",personService.listPersons(pageable,queryPerson,college_id));
        if (!"".equals(appearance) && "officialM".equals(appearance)){
            return "collegeadmin/partymemberone";
        }else if (!"".equals(appearance) && "prepareM".equals(appearance)){
            return "collegeadmin/partymembertwo";
        }else if (!"".equals(appearance) && "positiveM".equals(appearance)){
            return "collegeadmin/partymemberthree";
        }
        //默认返回党员
        return "collegeadmin/partymemberone";
    }


    //后端查询之后展示
    @PostMapping("/persons/search/{appearance}")
    @OperationLogAnnotation(operaType = "搜索",operaDesc = "搜索党员信息")
    public String search(@PageableDefault(size = 5,sort = {"id"},direction = Sort.Direction.DESC) Pageable pageable,
                         @PathVariable String appearance,Model model, QueryPerson queryPerson, HttpSession session){
        //获取登录的管理员
        Admins loginAdmin = (Admins)session.getAttribute("admin");
        //默认只能看见学校办公室
        Integer college_id = 1;
        //获取管理员所在的学院
        if (loginAdmin!=null){
            college_id = loginAdmin.getCollege_id();
        }
        queryPerson.setAppearance(appearance);
        model.addAttribute("listMajor",majorService.listMajors(college_id));
        model.addAttribute("page",personService.listPersons(pageable,queryPerson,college_id));
        if (!"".equals(appearance) && "officialM".equals(appearance)){
            return "collegeadmin/partymemberone :: officialMList ";
        }else if (!"".equals(appearance) && "prepareM".equals(appearance)){
            return "collegeadmin/partymembertwo :: prepareMList";
        }else if (!"".equals(appearance) && "positiveM".equals(appearance)){
            return "collegeadmin/partymemberthree :: positiveMList";
        }
        return "collegeadmin/partymemberone :: officialMList";
    }


    //前往新增
    @GetMapping("/personInput/{appearance}")
    public String toPersonInput(@PathVariable String appearance, Model model,HttpSession session){
        //获取登录的管理员
        Admins loginAdmin = (Admins)session.getAttribute("admin");
        //默认只能看见学校办公室
        Integer college_id = 1;
        //获取管理员所在的学院
        if (loginAdmin!=null){
            college_id = loginAdmin.getCollege_id();
        }
        model.addAttribute("listMajor",majorService.listMajors(college_id));
        //防止和编辑的时候产生空指针异常
        model.addAttribute("persons",new Persons());
        //携带回去政治面貌
        model.addAttribute("appearance",appearance);
        return "collegeadmin/partymemberinput";
    }

    //新增
    @PostMapping("/persons/{appearance}")
    @OperationLogAnnotation(operaType = "新增",operaDesc = "新增党支部人员")
    public String PeresonsInput(@Valid Persons persons, BindingResult result,
                                @PathVariable String appearance,@RequestParam String originalpassword,RedirectAttributes attributes,
                                Model model,HttpSession session){
        //获取登录的管理员
        Admins loginAdmin = (Admins)session.getAttribute("admin");
        //默认只能看见学校办公室
        Integer college_id = 1;
        //获取管理员所在的学院
        if (loginAdmin!=null){
            college_id = loginAdmin.getCollege_id();
        }
        //只校验用户名重复
        if (personService.getPersonsByUsername(persons.getUsername())!=null){
            result.rejectValue("username","nameError","该用户名已经存在");
        }
        if (result.hasErrors()){
            model.addAttribute("listMajor",majorService.listMajors(college_id));
            return "collegeadmin/partymemberinput";
        }
        //设置政治面貌和学院
        persons.setAppearance(appearance);
        persons.setCollege_id(college_id);
        //加密
        persons.setPassword(new MD5Util().md5(originalpassword));
        Persons p = personService.savePersons(persons);
        if (p!=null && "officialM".equals(appearance)){
            attributes.addFlashAttribute("message","新增党员成功");
            return "redirect:/admin/collegeAdmin/persons/officialM";
        }else if (p!=null && "prepareM".equals(appearance)){
            attributes.addFlashAttribute("message","新增预备党员成功");
            return "redirect:/admin/collegeAdmin/persons/prepareM";
        }else if (p!=null && "positiveM".equals(appearance)){
            attributes.addFlashAttribute("message","新增积极分子成功");
            return "redirect:/admin/collegeAdmin/persons/positiveM";
        }
        return "redirect:/admin/collegeAdmin/persons/officialM";
    }

    //前往修改
    @GetMapping("/personsUpdate/{id}")
    public String toPersonsUpdate(@PathVariable Integer id,Model model,HttpSession session){
        //获取登录的管理员
        Admins loginAdmin = (Admins)session.getAttribute("admin");
        //默认只能看见学校办公室
        Integer college_id = 1;
        //获取管理员所在的学院
        if (loginAdmin!=null){
            college_id = loginAdmin.getCollege_id();
        }
        model.addAttribute("appearance",personService.getPersonsById(id).getAppearance());
        model.addAttribute("listMajor",majorService.listMajors(college_id));
        model.addAttribute("persons",personService.getPersonsById(id));
        return "collegeadmin/partymemberinput";
    }

    //修改
    @PostMapping("/personsUpdate/{id}")
    @OperationLogAnnotation(operaType = "修改",operaDesc = "修改党支部人员信息")
    public String personUpdate(@Valid Persons persons,BindingResult result,
                               @PathVariable Integer id,@RequestParam String originalpassword,RedirectAttributes attributes,Model model){
        MD5Util md5Util = new MD5Util();
        //数据库中获取的人员
        Persons dataPerson = personService.getPersonsById(id);
        //原始密码
        String password = dataPerson.getPassword();
        //如果输入的原始密码和数据库中不匹配，那么判定错误，不可修改信息
        if (!password.equals(md5Util.md5(originalpassword)) ){
            model.addAttribute("appearance",dataPerson.getAppearance());
            model.addAttribute("listMajor",majorService.listMajors(dataPerson.getCollege_id()));
            model.addAttribute("persons",personService.getPersonsById(id));
            model.addAttribute("errorMessage","原始密码错误，不可修改信息!");
            return "collegeadmin/partymemberinput";
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
        //设置政治面貌不变并且返回相应的政治面貌对应的页面
        String appearance = dataPerson.getAppearance();
        persons.setAppearance(appearance);
        Persons p = personService.updatePersons(id, persons);
        if (p!=null && "officialM".equals(appearance)){
            attributes.addFlashAttribute("message","修改党员成功");
            return "redirect:/admin/collegeAdmin/persons/officialM";
        }else if (p!=null && "prepareM".equals(appearance)){
            attributes.addFlashAttribute("message","修改预备党员成功");
            return "redirect:/admin/collegeAdmin/persons/prepareM";
        }else if (p!=null && "positiveM".equals(appearance)){
            attributes.addFlashAttribute("message","修改积极分子成功");
            return "redirect:/admin/collegeAdmin/persons/positiveM";
        }
        return "redirect:/admin/collegeAdmin/persons/officialM";
    }


    //删除
    @GetMapping("/personsDelete/{id}")
    @OperationLogAnnotation(operaType = "删除",operaDesc = "删除单个人员信息")
    public String personsDelete(@PathVariable Integer id,RedirectAttributes attributes){
        //数据库中获取的人员
        Persons dataPerson = personService.getPersonsById(id);
        //获得政治面貌并且返回相应的政治面貌对应的页面
        String appearance = dataPerson.getAppearance();
        personService.deletePersons(id);
        if ("officialM".equals(appearance)){
            attributes.addFlashAttribute("message","删除党员成功");
            return "redirect:/admin/collegeAdmin/persons/officialM";
        }else if ("prepareM".equals(appearance)){
            attributes.addFlashAttribute("message","删除预备党员成功");
            return "redirect:/admin/collegeAdmin/persons/prepareM";
        }else if ("positiveM".equals(appearance)){
            attributes.addFlashAttribute("message","删除积极分子成功");
            return "redirect:/admin/collegeAdmin/persons/positiveM";
        }
        return "redirect:/admin/collegeAdmin/persons/officialM";
    }


    //批量删除
    @GetMapping("/personsDeleteSome/{checkedID}")
    @OperationLogAnnotation(operaType = "删除",operaDesc = "批量删除人员信息")
    public String personsDeleteSome(@PathVariable String checkedID,RedirectAttributes attributes){
        //切分id，并且逐条删除
        String[] splitId = checkedID.split(",");
        //数据库中获取的这批人员的政治面貌（至少有一个人员，所以获取第一个的政治面貌）
        Persons dataPerson = personService.getPersonsById(Integer.parseInt(splitId[0]));
        //获得政治面貌并且返回相应的政治面貌对应的页面
        String appearance = dataPerson.getAppearance();
        for (String id:splitId){
            personService.deletePersons(Integer.parseInt(id));
        }
        if ("officialM".equals(appearance)){
            attributes.addFlashAttribute("message","删除"+splitId.length+"个党员成功");
            return "redirect:/admin/collegeAdmin/persons/officialM";
        }else if ("prepareM".equals(appearance)){
            attributes.addFlashAttribute("message","删除"+splitId.length+"个预备党员成功");
            return "redirect:/admin/collegeAdmin/persons/prepareM";
        }else if ("positiveM".equals(appearance)){
            attributes.addFlashAttribute("message","删除"+splitId.length+"个积极分子成功");
            return "redirect:/admin/collegeAdmin/persons/positiveM";
        }
        return "redirect:/admin/collegeAdmin/persons/officialM";
    }





    //前往转正和转预
    @GetMapping("/updateAppearance/{id}")
    public String toPersonsUpdateAppearance(@PathVariable Integer id,Model model){
        model.addAttribute("persons",personService.getPersonsById(id));
        model.addAttribute("appearance",personService.getPersonsById(id).getAppearance());
        return "collegeadmin/updateappearance";
    }

    //转正或者转预
    @PostMapping("/updateAppearance/{id}")
    @OperationLogAnnotation(operaType = "修改",operaDesc = "转预或转正")
    public String personsUpdateAppearance(String updatetime,@PathVariable Integer id,
                                          RedirectAttributes attributes) throws ParseException {
        System.out.println("接收到的时间:"+updatetime);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date formatTime = format.parse(updatetime);
        Persons databasePerson = personService.getPersonsById(id);
        String appearance = databasePerson.getAppearance();
        if ("prepareM".equals(appearance)){
            databasePerson.setJoinpartytime(formatTime);
            databasePerson.setAppearance("officialM");
            personService.updatePersons(id,databasePerson);
            attributes.addFlashAttribute("message","转正成功");
            return "redirect:/admin/collegeAdmin/persons/officialM";
        }else if ("positiveM".equals(appearance)){
            databasePerson.setPreparejoinpartytime(formatTime);
            databasePerson.setAppearance("prepareM");
            personService.updatePersons(id,databasePerson);
            attributes.addFlashAttribute("message","转预成功");
            return "redirect:/admin/collegeAdmin/persons/prepareM";
        }
        return "redirect:/admin/collegeAdmin/persons/officialM";
    }


    //可视化图表展示
    @GetMapping("/personsChart")
    @OperationLogAnnotation(operaType = "查看",operaDesc = "查看党员比例饼状图")
    public String personsChart(HttpSession session,Model model){
        //获取登录的管理员
        Admins loginAdmin = (Admins)session.getAttribute("admin");
        //默认只能看见学校办公室
        Integer college_id = 1;
        //获取管理员所在的学院
        if (loginAdmin!=null){
            college_id = loginAdmin.getCollege_id();
        }
        //用来存放数量
        List<Persons> officialList = personService.listPersons(college_id, "officialM");
        List<Persons> prepareList = personService.listPersons(college_id, "prepareM");
        List<Persons> positiveList = personService.listPersons(college_id, "positiveM");
        List<Integer> listNums = new ArrayList<>();
        listNums.add(officialList.size());
        listNums.add(prepareList.size());
        listNums.add(positiveList.size());
        List<String> listName = new ArrayList<>();
        listName.add("中共党员");
        listName.add("预备党员");
        listName.add("积极分子");
        model.addAttribute("nameJson",JSON.toJSONString(listName));
        model.addAttribute("numsJson",JSON.toJSONString(listNums));

        model.addAttribute("sumPerson",JSON.toJSONString(listNums.get(0)+listNums.get(1)+listNums.get(2)));
        return "collegeadmin/partymembershow";
    }



}
