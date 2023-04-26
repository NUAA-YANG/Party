package com.yzx.party.dao;

import com.yzx.party.pojo.Statements;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * @author yzx
 * @version JDK 1.8.0_131
 * @since 2022/2/13 10:17
 */
public interface StatementRepository extends JpaRepository<Statements,Integer>, JpaSpecificationExecutor<Statements> {
    //根据会议id查询出所有的评论
    @Query("select stt from Statements stt where stt.meeting_id=?1 order by stt.speaktime desc ")
    List<Statements> findByMeeting_id(Integer meeting_id);
}
