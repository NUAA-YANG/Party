package com.yzx.party.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * @author yzx
 * @version JDK 1.8.0_131
 * @since 2022/2/17 11:12
 */
//这个注解表示了这是一个Mybaits的mapper类
@Mapper
//加入仓库管理才能使用
@Repository
public interface DeleteLogBySizeMapper {
    //后端删除指定数量的日志
    void deleteLogBySize(Integer size);

    //后端删除全部日志
    void deleteAllLog();
}
