package com.yzx.party.service;

import com.yzx.party.dao.PersonRepository;
import com.yzx.party.myException.NotFoundException;
import com.yzx.party.pojo.Majors;
import com.yzx.party.pojo.Persons;
import com.yzx.party.vo.QueryPerson;
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
 * @since 2022/1/12 9:25
 */
@Service
public class PersonServiceImpl implements PersonService {
    @Autowired
    PersonRepository personRepository;

    //加入事务操作
    @Transactional
    @Override
    public Page<Persons> listPersons(Pageable pageable, QueryPerson queryPerson,Integer college_id) {
        return personRepository.findAll(new Specification<Persons>() {
            @Override
            public Predicate toPredicate(Root<Persons> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {
                List<Predicate> predicates = new ArrayList<>();
                //姓名
                if (!"".equals(queryPerson.getName()) && queryPerson.getName()!=null){
                    predicates.add(cb.like(root.get("name"),"%"+queryPerson.getName()+"%"));
                }
                //专业
                if (queryPerson.getMajor_id()!=null){
                    predicates.add(cb.equal(root.get("major_id"),queryPerson.getMajor_id()));
                }
                //年级
                if (queryPerson.getGrade()!=null){
                    predicates.add(cb.equal(root.get("grade"),queryPerson.getGrade()));
                }
                //根据政治面貌查询
                if (!"".equals(queryPerson.getAppearance()) && queryPerson.getAppearance()!=null){
                    predicates.add(cb.equal(root.get("appearance"),queryPerson.getAppearance()));
                }
                //只能查询所在学院的人的信息
                predicates.add(cb.equal(root.get("college_id"),college_id));
                cq.where(predicates.toArray(new Predicate[predicates.size()]));
                return null;
            }
        },pageable);
    }

    //加入事务操作
    @Transactional
    @Override
    public List<Persons> listPersons(Integer college_id) {
        return personRepository.findPersonsByCollege_id(college_id);
    }

    //加入事务操作
    @Transactional
    @Override
    public List<Persons> listPersons(Integer college_id, String appearance) {
        return personRepository.findPersonsByCollege_idAAndAppearance(college_id, appearance);
    }

    //加入事务操作
    @Transactional
    @Override
    public Persons savePersons(Persons persons) {
        return personRepository.save(persons);
    }

    //加入事务操作
    @Transactional
    @Override
    public Persons getPersonsById(Integer id) {
        return personRepository.findById(id).get();
    }

    //加入事务操作
    @Transactional
    @Override
    public Persons getPersonsByUsername(String name) {
        return personRepository.findByUsername(name);
    }

    @Override
    public Persons getPersonsByUsernameAndPassword(String username, String password) {
        return personRepository.findByUsernameAndPassword(username, password);
    }

    @Override
    public Persons getPersonsByUsernameAndEmail(String username, String email) {
        return personRepository.findByUsernameAndEmail(username, email);
    }

    //加入事务操作
    @Transactional
    @Override
    public Persons updatePersons(Integer id, Persons persons) {
        Persons p = personRepository.findById(id).get();
        if (p==null){
            throw new NotFoundException("不存在该类型");
        }
        BeanUtils.copyProperties(persons,p);
        return personRepository.save(p);
    }

    //加入事务操作
    @Transactional
    @Override
    public void deletePersons(Integer id) {
        personRepository.deleteById(id);
    }
}
