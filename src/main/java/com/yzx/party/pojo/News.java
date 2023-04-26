package com.yzx.party.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

/**
 * @author yzx
 * @version JDK 1.8.0_131
 * @since 2022/1/5 16:01
 */
//新闻实体类
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "t_news")
public class News {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 主键自增长
    private Integer id;
    private String ntype;//新闻类型 原创还是转载
    private String published;//记录草稿或者发布
    private String title;
    private String content;//内容
    private String introduce;//新闻介绍（用于前台展示简短的内容）
    private Integer college_id; //所属学院
    private Integer major_id;//所属专业
    private String indexpicture;//首图引用地址
    private Integer views;//文章的浏览次数
    @Temporal(TemporalType.TIMESTAMP)//更新时间
    private Date updatetime;
    private Integer admin_id;//发布者id
}

