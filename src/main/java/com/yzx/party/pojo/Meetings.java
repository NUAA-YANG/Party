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
 * @since 2022/2/7 17:06
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "t_meetings")
public class Meetings {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 主键自增长
    private Integer id;
    private String title;//会议名称
    //会议类型：支部党员大会、支委会、党小组会、党课以及其他
    private String mtype;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date starttime;//会议开始时间
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endtime;//会议结束时间
    private String address;//会议地址
    private Integer nums;//参会人数
    private String content;//会议内容
    private Integer college_id;//会议所属学院
    private Integer admin_id;//会议发布者
    private Double duration;//单场会议个持续时间(单位为小时)
}
