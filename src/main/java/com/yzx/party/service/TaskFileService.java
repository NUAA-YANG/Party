package com.yzx.party.service;

import com.yzx.party.pojo.TaskFile;
import com.yzx.party.pojo.Tasks;
import com.yzx.party.vo.QueryTask;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * @author yzx
 * @version JDK 1.8.0_131
 * @since 2022/3/11 10:12
 */
public interface TaskFileService {
    //后端的查询分页
    Page<TaskFile> listTaskFiles(Pageable pageable, QueryTask queryTask);
    List<TaskFile> allListTaskFiles();
    //新增
    TaskFile saveTaskFile(TaskFile taskFile);
    //根据id查询
    TaskFile getTaskFileById(Integer id);
    //根据名字查询
    TaskFile getTaskFileByFileName(String fileName);
    //修改
    TaskFile updateTaskFile(Integer id,TaskFile taskFile);
    //删除
    void deleteTaskFile(Integer id);
}
