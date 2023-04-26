package com.yzx.party.mapper;


import com.yzx.party.vo.PersonsTable;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author yzx
 * @version JDK 1.8.0_131
 * @since 2022/1/15 10:03
 */
//后端超级管理员展示人员分布数据
//这个注解表示了这是一个Mybaits的mapper类
@Mapper
//加入仓库管理才能使用
@Repository
public interface PersonsTableMapper {
    //后端超级管理员展示专业的政治面貌对应的人员分布
    PersonsTable findPersonsTable(Integer college_id,String appearance);
    //获得所有的学院id，用于遍历总共几个学院存在党员数据
    List<Integer> listCollege_id();
}


