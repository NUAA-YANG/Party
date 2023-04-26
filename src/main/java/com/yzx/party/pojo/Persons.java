package com.yzx.party.pojo;

import cn.afterturn.easypoi.excel.annotation.Excel;
import cn.afterturn.easypoi.excel.annotation.ExcelIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * @author yzx
 * @version JDK 1.8.0_131
 * @since 2022/1/5 16:05
 */
//人员实体类
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "t_persons")
public class Persons implements Serializable {
    //width表示导出的excel的宽度，orderNum表示对列排序
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 主键自增长
    @Excel(name = "数据库id",width = 10,orderNum = "1")
    private Integer id;
    @Excel(name = "登录用户名",width = 20,orderNum = "2")
    private String username;
    @Excel(name = "登录密码",width = 20,orderNum = "3")
    private String password;
    @Excel(name = "姓名",width = 20,orderNum = "4")
    private String name;
    @Excel(name = "政治面貌",width = 20,orderNum = "5")
    private String appearance;//政治面貌
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "申请入党时间",width = 25,exportFormat = "yyyy-MM-dd",orderNum = "6")
    private Date applyjoinpartytime;//申请入党时间
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "转预时间",width = 25,exportFormat = "yyyy-MM-dd",orderNum = "7")
    private Date preparejoinpartytime;//转预时间
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "转正时间",width = 25,exportFormat = "yyyy-MM-dd",orderNum = "8")
    private Date joinpartytime;//转正时间
    @Excel(name = "电话号码",width = 20,orderNum = "9")
    private String phone;
    @Excel(name = "邮箱",width = 25,orderNum = "10")
    private String email;
    @ExcelIgnore
    private Integer college_id;//所属学院，导出时不输出
    @ExcelIgnore
    private Integer major_id;//所属专业，导出时不输出
    @Excel(name = "详细班级|详细部门",width = 25,orderNum = "12")
    private String classname;//班级
    @Excel(name = "年级",width = 15,orderNum = "11")
    private Integer grade;//年级
}
