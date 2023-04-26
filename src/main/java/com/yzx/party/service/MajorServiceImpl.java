package com.yzx.party.service;

import com.yzx.party.dao.MajorRepository;
import com.yzx.party.myException.NotFoundException;
import com.yzx.party.pojo.Majors;
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
 * @since 2022/1/10 11:00
 */
@Service
public class MajorServiceImpl implements MajorService {
    @Autowired
    MajorRepository majorRepository;

    @Transactional
    @Override
    public Page<Majors> listMajors(Pageable pageable, Integer major_id,Integer college_id) {
        return majorRepository.findAll(new Specification<Majors>() {
            @Override
            public Predicate toPredicate(Root<Majors> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {
                List<Predicate> predicates = new ArrayList<>();
                //专业的模糊查询
                if (major_id!=null){
                    //这里的id是我们放入Page<Majors>对象的属性
                    predicates.add(cb.equal(root.<Integer>get("id"),major_id));
                }
                //只能查询到所在学院的专业
                predicates.add(cb.equal(root.get("college_id"),college_id));
                cq.where(predicates.toArray(new Predicate[predicates.size()]));
                return null;
            }
        },pageable);
    }

    @Transactional
    @Override
    public Page<Majors> listMajors(Pageable pageable) {
        return majorRepository.findAll(pageable);
    }

    @Transactional
    @Override
    public List<Majors> listMajors(Integer college_id) {
        return majorRepository.findMajorByCollege_id(college_id);
    }

    @Override
    public Majors saveMajors(Majors majors) {
        return majorRepository.save(majors);
    }

    @Transactional
    @Override
    public Majors getMajorById(Integer id) {
        return majorRepository.findById(id).get();
    }

    @Transactional
    @Override
    public Majors getMajorByName(String name) {
        return majorRepository.findByName(name);
    }

    @Transactional
    @Override
    public Majors getMajorByNameAndCreatePartyTime(String name, Date createpartytime) {
        return majorRepository.findByNameAndCreatepartytime(name, createpartytime);
    }

    @Transactional
    @Override
    public Majors updateMajors(Integer id, Majors majors) {
        Majors m = majorRepository.findById(id).get();
        if (m==null){
            throw new NotFoundException("不存在该类型");
        }
        BeanUtils.copyProperties(majors,m);
        return majorRepository.save(m);
    }

    @Transactional
    @Override
    public void deleteMajors(Integer id) {
        majorRepository.deleteById(id);
    }
}
