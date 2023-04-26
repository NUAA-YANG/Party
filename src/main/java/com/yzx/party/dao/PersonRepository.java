package com.yzx.party.dao;

import com.yzx.party.pojo.Persons;
import com.yzx.party.service.PersonService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * @author yzx
 * @version JDK 1.8.0_131
 * @since 2022/1/12 9:12
 */
//后台人员管理
public interface PersonRepository extends JpaRepository<Persons,Integer>, JpaSpecificationExecutor<Persons> {

    Persons findByUsername(String username);

    //验证登录
    Persons findByUsernameAndPassword(String username,String password);

    //找回密码
    Persons findByUsernameAndEmail(String username,String email);

    @Query("select p from Persons p where p.college_id=?1")
    List<Persons> findPersonsByCollege_id(Integer college_id);

    @Query("select p from Persons p where p.college_id=?1 and p.appearance like ?2")
    List<Persons> findPersonsByCollege_idAAndAppearance(Integer college_id,String appearance);
}
