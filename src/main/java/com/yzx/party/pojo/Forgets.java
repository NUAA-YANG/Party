package com.yzx.party.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

/**
 * @author yzx
 * @version JDK 1.8.0_131
 * @since 2022/3/14 11:00
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "t_forgets")
public class Forgets {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 主键自增长
    private Integer id;
    private String username;
    private String email;
    private Date createtime;//创建时间
    private String flag;//admin和person用于区分管理员和用户
    private Date invalidtime;//失效时间，自动生成为【创建时间+5分钟】
    private String varcode;//验证码
}
