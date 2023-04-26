package com.yzx.party.service;

import com.yzx.party.pojo.Persons;
import com.yzx.party.vo.QueryPerson;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * @author yzx
 * @version JDK 1.8.0_131
 * @since 2022/1/12 9:14
 */
//后台人员管理
public interface PersonService {
    //后台分页人员(分页参数，查询参数，所在学院id)
    Page<Persons> listPersons(Pageable pageable, QueryPerson queryPerson,Integer college_id);
    List<Persons> listPersons(Integer college_id);
    List<Persons> listPersons(Integer college_id,String appearance);
    //新增
    Persons savePersons(Persons persons);
    //根据内容查询
    Persons getPersonsById(Integer id);
    Persons getPersonsByUsername(String name);
    Persons getPersonsByUsernameAndPassword(String username,String password);
    //用于找回密码
    Persons getPersonsByUsernameAndEmail(String username,String email);
    //修改
    Persons updatePersons(Integer id,Persons persons);
    //删除
    void deletePersons(Integer id);
}
