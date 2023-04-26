package com.yzx.party.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.Date;
import java.util.Objects;

/**
 * @author yzx
 * @version JDK 1.8.0_131
 * @since 2022/1/5 15:47
 */
//管理员实体类
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "t_admins")
public class Admins {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;// 主键自增长
    @NotBlank(message = "用户名不能为空")
    private String username;//登录用户名
    private String password;
    private String name; //管理人员姓名
    private String phone;
    private String email;
    private Integer college_id; //所属学院id
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date joinpartytime;//入党时间
    //标志位 2为学校管理员；1为学院管理员
    private String flag;
}

