package com.yzx.party.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author yzx
 * @version JDK 1.8.0_131
 * @since 2022/3/19 10:19
 */
//改进版，用于学校管理员新闻雷达图展示
@Data
@AllArgsConstructor
@NoArgsConstructor
public class NewsChartTwo {
    private Integer id;//学院id
    private String name;//学院名称
    private Integer nums;//新闻发布数量
    private Integer viewsNum;//新闻总的阅读量
    private Integer commentsNum;//新闻总的评论数
}
