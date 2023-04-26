package com.yzx.party.service;

import com.yzx.party.pojo.Tasks;
import com.yzx.party.vo.QueryTask;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * @author yzx
 * @version JDK 1.8.0_131
 * @since 2022/3/10 9:59
 */

public interface TaskService {
    //后端的查询分页
    Page<Tasks> listTasks(Pageable pageable, QueryTask queryTask);
    List<Tasks> listTask();
    //新增
    Tasks saveTask(Tasks tasks);
    //根据id查询
    Tasks getTaskById(Integer id);
    //修改
    Tasks updateTasks(Integer id,Tasks tasks);
    //删除
    void deleteTask(Integer id);
}
