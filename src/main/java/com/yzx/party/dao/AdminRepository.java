package com.yzx.party.dao;

import com.yzx.party.pojo.Admins;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * @author yzx
 * @version JDK 1.8.0_131
 * @since 2022/1/5 16:17
 */
//检查登录
public interface AdminRepository extends JpaRepository<Admins,Integer>, JpaSpecificationExecutor<Admins> {


    //用于验证登录
    Admins findByUsernameAndPassword(String username,String password);

    Admins findByUsername(String username);

    //用于找回密码
    Admins findByUsernameAndEmail(String username,String email);

    //获得对应学院的管理员
    @Query("select a from Admins a where a.college_id=?1")
    List<Admins> listAdminsByCollege_id(Integer college_id);
}
