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
 * @since 2022/3/10 9:37
 */
//任务实体类
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "t_tasks")
public class Tasks {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 主键自增长
    private Integer id;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date publictime;//任务发布时间
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date endtime;//任务额定结束时间
    private String content;//任务内容
    private Integer remind;//提醒
    private  Integer admin_id;//发布管理员
    private Integer college_id;//所属学院
    private String title;//任务标题
    private String compcollege_id;//完成任务的学院id，起始为空，每一个学院完成任务就添加该学院id
}
