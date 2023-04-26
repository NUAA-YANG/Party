package com.yzx.party.dao;

import com.yzx.party.pojo.Meetings;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * @author yzx
 * @version JDK 1.8.0_131
 * @since 2022/2/7 17:23
 */
public interface MeetingRepository extends JpaRepository<Meetings,Integer>, JpaSpecificationExecutor<Meetings> {

    @Query("select m from Meetings m where m.college_id=?1 order by m.endtime desc ")
    List<Meetings> findByCollege_id(Integer college_id);
}
