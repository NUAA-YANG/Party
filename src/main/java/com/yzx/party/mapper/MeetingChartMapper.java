package com.yzx.party.mapper;

import com.yzx.party.vo.MeetingChart;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author yzx
 * @version JDK 1.8.0_131
 * @since 2022/2/15 10:47
 */
//这个注解表示了这是一个Mybaits的mapper类
@Mapper
//加入仓库管理才能使用
@Repository
public interface MeetingChartMapper {
    //后端的折线图展示三会一课的总的时长（在超级管理员中,size表示筛选多少个）
    List<MeetingChart> findAllTop(Integer size);
}


