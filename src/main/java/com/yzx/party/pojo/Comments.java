package com.yzx.party.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

/**
 * @author yzx
 * @version JDK 1.8.0_131
 * @since 2022/1/5 15:57
 */
//评论实体类
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "t_comments")
public class Comments {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 主键自增长
    private Integer id;
    private Integer new_id;//评论属于哪个新闻
    private String nickname;//发表评论的人的昵称
    private String content;//评论内容
    @Temporal(TemporalType.TIMESTAMP)
    private Date creattime;//评论发表时间
    private Integer parent_id;//父评论id
    private String phone;//电话
    private Integer college_id;//所属学院id
    private Integer person_id;//属于哪个人
    private String title;//新闻标题
}

