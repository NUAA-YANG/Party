package com.yzx.party.service;

import com.yzx.party.pojo.Colleges;
import com.yzx.party.pojo.Majors;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Date;
import java.util.List;

/**
 * @author yzx
 * @version JDK 1.8.0_131
 * @since 2022/1/10 10:54
 */
//下划党支部接口
public interface MajorService {
    //后端的分页下划党支部（附带查询）
    Page<Majors> listMajors(Pageable pageable,Integer major_id,Integer college_id);
    //后端的分页下划党支部（无查询）
    Page<Majors> listMajors(Pageable pageable);
    //就是查询全部
    List<Majors> listMajors(Integer college_id);
    //新增
    Majors saveMajors(Majors majors);
    //根据id查询下划党支部
    Majors getMajorById(Integer id);
    Majors getMajorByName(String name);
    Majors getMajorByNameAndCreatePartyTime(String name, Date createpartytime);
    //修改下划党支部信息
    Majors updateMajors(Integer id,Majors majors);
    //删除
    void deleteMajors(Integer id);
}
