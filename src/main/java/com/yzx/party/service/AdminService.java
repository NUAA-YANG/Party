package com.yzx.party.service;

import com.yzx.party.pojo.Admins;
import com.yzx.party.vo.QueryAdmin;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * @author yzx
 * @version JDK 1.8.0_131
 * @since 2022/1/5 15:45
 */

public interface AdminService {
    //用户登录验证
    Admins checkAdmin(String username,String password);
    //后端的分页管理员
    Page<Admins> listAdmins(Pageable pageable , QueryAdmin queryAdmin);
    //查询全部
    List<Admins> listAdmins();
    //根据学院获得所有的管理员
    List<Admins> listAdmins(Integer college_id);
    //新增
    Admins saveAdmins(Admins admins);
    //根据id查询
    Admins getAdminById(Integer id);
    //根据用户名查询
    Admins getAdminByUsername(String username);
    //根据用户名和邮箱查询，用于找回密码
    Admins getAdminByUsernameAndEmail(String username,String email);
    //修改
    Admins updateAdmins(Integer id,Admins admins);
    //删除
    void deleteAdmins(Integer id);
}

