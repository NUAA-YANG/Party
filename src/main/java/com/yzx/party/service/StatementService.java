package com.yzx.party.service;

import com.yzx.party.pojo.Statements;
import com.yzx.party.vo.QueryCS;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * @author yzx
 * @version JDK 1.8.0_131
 * @since 2022/2/13 10:19
 */
public interface StatementService {
    //展示所有的发言
    Page<Statements> listStatements(Pageable pageable, QueryCS queryCS);
    //根据不同的会议展示出该会议的所有评论
    List<Statements> listStatements(Integer meeting_id);
    //新增
    Statements saveStatement(Statements statements);
    //删除
    void deleteStatements(Integer id);
}
