package com.yzx.party.pojo;

import cn.afterturn.easypoi.excel.annotation.Excel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

/**
 * @author yzx
 * @version JDK 1.8.0_131
 * @since 2022/2/13 10:12
 */
//三会一课发言实体类
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "t_statements")
public class Statements {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 主键自增长
    private Integer id;
    @Temporal(TemporalType.TIMESTAMP)
    private Date speaktime;//发言时间
    private Integer meeting_id;//所属会议
    private String nickname;//昵称
    private String content; //内容
    private String phone;//电话
    private Integer college_id;//所属学院
    private String title;//会议标题
}
