package com.yzx.party.dao;

import com.yzx.party.pojo.Majors;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;

/**
 * @author yzx
 * @version JDK 1.8.0_131
 * @since 2022/1/10 10:53
 */
//后台专业管理
public interface MajorRepository extends JpaRepository<Majors,Integer>, JpaSpecificationExecutor<Majors> {

    Majors findByName(String name);

    Majors findByNameAndCreatepartytime(String name, Date createPartyTime);

    @Query("select m from Majors m where m.college_id=?1")
    List<Majors> findMajorByCollege_id(Integer college_id);
}
