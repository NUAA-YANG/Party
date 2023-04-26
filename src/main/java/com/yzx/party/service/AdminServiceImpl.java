package com.yzx.party.service;

import com.yzx.party.dao.AdminRepository;
import com.yzx.party.myException.NotFoundException;
import com.yzx.party.pojo.Admins;
import com.yzx.party.pojo.News;
import com.yzx.party.vo.QueryAdmin;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

/**
 * @author yzx
 * @version JDK 1.8.0_131
 * @since 2022/1/5 16:36
 */
@Service
public class AdminServiceImpl implements AdminService {
    @Autowired
    AdminRepository adminRepository;

    //加入事务操作
    @Transactional
    @Override
    public Admins checkAdmin(String username, String password) {
        Admins admins = adminRepository.findByUsernameAndPassword(username, password);
        return admins;
    }

    //模糊查询
    @Transactional
    @Override
    public Page<Admins> listAdmins(Pageable pageable, QueryAdmin queryAdmin) {
        return adminRepository.findAll(new Specification<Admins>() {
            @Override
            public Predicate toPredicate(Root<Admins> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {
                List<Predicate> predicates = new ArrayList<>();
                if (!"".equals(queryAdmin.getName()) && queryAdmin.getName()!=null){
                    predicates.add(cb.like(root.get("name"),"%"+queryAdmin.getName()+"%"));
                }
                if (queryAdmin.getCollege_id()!=null){
                    predicates.add(cb.equal(root.get("college_id"),queryAdmin.getCollege_id()));
                }
                if (queryAdmin.getMajor_id()!=null){
                    predicates.add(cb.equal(root.get("major_id"),queryAdmin.getMajor_id()));
                }
                cq.where(predicates.toArray(new Predicate[predicates.size()]));
                return null;
            }
        },pageable);
    }


    @Transactional
    @Override
    public List<Admins> listAdmins() {
        return adminRepository.findAll();
    }

    @Transactional
    @Override
    public List<Admins> listAdmins(Integer college_id) {
        return adminRepository.listAdminsByCollege_id(college_id);
    }

    @Transactional
    @Override
    public Admins saveAdmins(Admins admins) {
        return adminRepository.save(admins);
    }

    @Transactional
    @Override
    public Admins getAdminById(Integer id) {
        return adminRepository.findById(id).get();
    }

    @Transactional
    @Override
    public Admins getAdminByUsername(String username) {
        return adminRepository.findByUsername(username);
    }

    @Transactional
    @Override
    public Admins getAdminByUsernameAndEmail(String username, String email) {
        return adminRepository.findByUsernameAndEmail(username, email);
    }

    @Transactional
    @Override
    public Admins updateAdmins(Integer id, Admins admins) {
        Admins a = adminRepository.findById(id).get();
        if (a==null){
            throw new NotFoundException("不存在该类型");
        }
        //复制一个对象
        BeanUtils.copyProperties(admins,a);
        //因为存在该id，所以再次储存的时候就相当于更新了数据
        return adminRepository.save(a);
    }

    @Transactional
    @Override
    public void deleteAdmins(Integer id) {
        adminRepository.deleteById(id);
    }
}
