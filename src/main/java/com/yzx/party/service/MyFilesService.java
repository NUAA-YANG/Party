package com.yzx.party.service;

import com.yzx.party.pojo.MyFiles;
import com.yzx.party.vo.QueryFile;
import com.yzx.party.vo.QueryNew;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * @author yzx
 * @version JDK 1.8.0_131
 * @since 2022/1/27 10:54
 */
public interface MyFilesService {
    //展示列表（附带查询）
    Page<MyFiles> listFiles(Pageable pageable, QueryFile queryFile);
    //保存（上传）文件
    MyFiles saveFiles(MyFiles files);
    //根据id获取文件
    MyFiles getFileById(Integer id);
    MyFiles getFileByTitle(String title);
    //删除
    void deleteFiles(Integer id);
}
