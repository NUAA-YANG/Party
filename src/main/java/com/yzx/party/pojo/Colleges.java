package com.yzx.party.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.Date;

/**
 * @author yzx
 * @version JDK 1.8.0_131
 * @since 2022/1/5 15:55
 */
//学院实体类
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "t_colleges")
public class Colleges {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 主键自增长
    private Integer id;
    @NotBlank(message = "学院党支部名称不能为空")
    private String name;
    //学院创建党支部时间
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date createpartytime;
}
