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
 * @since 2022/3/11 10:02
 */
//用于接收完成任务情况
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "t_taskfile")
public class TaskFile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 主键自增长
    private Integer id;
    private String file;//文件名称
    private Integer admin_id;//完成任务管理员id
    private Integer college_id;//所属学院id
    private String content;//任务完成内容
    private String title;//任务名称
    @DateTimeFormat(pattern = "yyyy-MM-dd hh:MM:ss")
    private Date comptime;//任务完成时间
    private Integer task_id;//所属任务
}
