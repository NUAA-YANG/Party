package com.yzx.party.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.Date;

/**
 * @author yzx
 * @version JDK 1.8.0_131
 * @since 2022/1/5 15:59
 */
//专业实体类
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "t_majors")
public class Majors {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 主键自增长
    private Integer id;
    private String name;
    //创建专业党支部时间
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date createpartytime;
    //所属学院
    private Integer college_id;
}
