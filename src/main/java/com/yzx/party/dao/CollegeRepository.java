package com.yzx.party.dao;


import com.yzx.party.pojo.Colleges;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Date;

/**
 * @author yzx
 * @version JDK 1.8.0_131
 * @since 2022/1/7 10:15
 */
public interface CollegeRepository extends JpaRepository<Colleges,Integer>, JpaSpecificationExecutor<Colleges> {
    //自定义根据名称查询
    Colleges findByName(String name);


    Colleges findByNameAndCreatepartytime(String name,Date createpartytime);
}
