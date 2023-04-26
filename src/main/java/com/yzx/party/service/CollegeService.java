package com.yzx.party.service;

import com.yzx.party.pojo.Colleges;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Date;
import java.util.List;

/**
 * @author yzx
 * @version JDK 1.8.0_131
 * @since 2022/1/7 10:16
 */
//学院接口
public interface CollegeService {
    //后端的分页学院（附带查询） 只能查询该管理员所在学院的专业
    Page<Colleges> listColleges(Pageable pageable,Integer college_id);
    //分页学院（没有查询）
    Page<Colleges> listColleges(Pageable pageable);
    //就是查询全部
    List<Colleges> listColleges();
    //新增学院
    Colleges saveColleges(Colleges colleges);
    //根据id查询学院
    Colleges getCollegeById(Integer id);
    //根据名称查询学院
    Colleges getCollegeByName(String name);
    //新增校对
    Colleges getCollegeByNameAndCreatePartyTime(String name, Date createpartytime);
    //修改学院信息
    Colleges updateColleges(Integer id,Colleges colleges);
    //删除
    void deleteColleges(Integer id);
}
