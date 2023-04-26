package com.yzx.party.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author yzx
 * @version JDK 1.8.0_131
 * @since 2022/1/10 10:11
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
//后端的树状图表展示
// 1、表示每个学院发布的新闻数量，在超级管理员中展示
// 2、表示每个专业发布的新闻数量，在学院管理员中以及前端中展示
public class NewsChart {
    private Integer id;//学院id
    private String name;//学院名称
    private Integer nums;//新闻数量
}

