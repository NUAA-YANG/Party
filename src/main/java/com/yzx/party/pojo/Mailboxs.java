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
 * @since 2022/2/18 10:34
 */
//书记信箱
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "t_mailboxs")
public class Mailboxs {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 主键自增长
    private Integer id;
    private String qtype;//类型(投诉、举报、求助、咨询、建议、其他)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date speaktime;//问题时间
    private String question;//问题内容
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date replaytime;//回复时间
    private String adminname; //回复者姓名
    private String answer;//回复内容
    private String state;//展示给后台管理员的状态（未读、已读、已回复）
    private Integer college_id;//所属学院
    private Integer person_id;//发布者id
    private String personname;//发布者姓名
    private String questionintro;//问题的简介
    private String answerintro;//回答的简介
    private String title;//信件标题
    private String statetwo;//展示给前台的状态（未回复、已回复）
}
