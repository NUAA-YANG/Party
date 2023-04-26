package com.yzx.party.mapper;

import com.yzx.party.vo.NewsChartTwo;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author yzx
 * @version JDK 1.8.0_131
 * @since 2022/3/19 10:22
 */
//这个注解表示了这是一个Mybaits的mapper类
@Mapper
//加入仓库管理才能使用
@Repository
public interface NewsChartTwoMapper {
    //用于超级管理员新闻堆叠图展示（新闻发布数量、新闻评论数量、新闻阅读数量），size表示展示几个学院
    List<NewsChartTwo> radarNews(Integer size);
}
