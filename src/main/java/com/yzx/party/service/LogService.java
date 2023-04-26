package com.yzx.party.service;

import com.yzx.party.pojo.Logs;
import com.yzx.party.vo.QueryLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * @author yzx
 * @version JDK 1.8.0_131
 * @since 2022/2/16 10:32
 */
public interface LogService {
    //展示所有的日志
    Page<Logs> listLogs(Pageable pageable, QueryLog queryLog);
    //新增
    Logs saveLogs(Logs logs);
    //删除
    void deleteLog(Integer id);
}
