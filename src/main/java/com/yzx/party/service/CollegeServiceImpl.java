package com.yzx.party.service;

import com.yzx.party.dao.CollegeRepository;
import com.yzx.party.myException.NotFoundException;
import com.yzx.party.pojo.Colleges;
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
import java.util.Date;
import java.util.List;

/**
 * @author yzx
 * @version JDK 1.8.0_131
 * @since 2022/1/7 10:29
 */
@Service
public class CollegeServiceImpl implements CollegeService {
    @Autowired
    CollegeRepository collegeRepository;

    @Transactional
    @Override
    public Page<Colleges> listColleges(Pageable pageable, Integer college_id) {
        return collegeRepository.findAll(new Specification<Colleges>() {
            @Override
            public Predicate toPredicate(Root<Colleges> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {
                List<Predicate> predicates = new ArrayList<>();
                //学院的模糊查询
                if (college_id!=null){
                    //这里的id是我们放入Page<Colleges>对象的属性
                    predicates.add(cb.equal(root.<Integer>get("id"),college_id));
                }
                cq.where(predicates.toArray(new Predicate[predicates.size()]));
                return null;
            }
        },pageable);
    }

    @Transactional
    @Override
    public Page<Colleges> listColleges(Pageable pageable) {
        return collegeRepository.findAll(pageable);
    }

    @Override
    public List<Colleges> listColleges() {
        return collegeRepository.findAll();
    }

    @Transactional
    @Override
    public Colleges saveColleges(Colleges colleges) {
        return collegeRepository.save(colleges);
    }

    @Transactional
    @Override
    public Colleges getCollegeById(Integer id) {
        return collegeRepository.findById(id).get();
    }

    @Override
    public Colleges getCollegeByName(String name) {
        return collegeRepository.findByName(name);
    }

    @Override
    public Colleges getCollegeByNameAndCreatePartyTime(String name, Date createpartytime) {
        return collegeRepository.findByNameAndCreatepartytime(name,createpartytime);
    }

    @Transactional
    @Override
    public Colleges updateColleges(Integer id, Colleges colleges) {
        Colleges c = collegeRepository.findById(id).get();
        if (c==null){
            throw new NotFoundException("不存在该类型");
        }
        //复制一个对象
        BeanUtils.copyProperties(colleges,c);
        return collegeRepository.save(c);
    }

    @Transactional
    @Override
    public void deleteColleges(Integer id) {
        collegeRepository.deleteById(id);
    }
}
