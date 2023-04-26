package com.yzx.party.mapper;

import com.yzx.party.vo.NewsChart;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author yzx
 * @version JDK 1.8.0_131
 * @since 2022/1/10 10:12
 */
//这个注解表示了这是一个Mybaits的mapper类
@Mapper
//加入仓库管理才能使用
@Repository
public interface NewsChartMapper {
    //后端的树状图表展示，表示每个学院发布的新闻数量，在超级管理员中展示，这里的id表示的是学院的id,size表示筛选多少个
    List<NewsChart> findAllTop(Integer size);
    //后端的树状图表展示，查询的是每个学院的专业发布的新闻数量，在管理员中展示；以及前端的分类展示；size表示筛选多少个
    List<NewsChart> foreachCollegeNews(Integer college_id,Integer size);
}

