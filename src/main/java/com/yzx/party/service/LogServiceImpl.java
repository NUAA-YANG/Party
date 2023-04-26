package com.yzx.party.service;

import com.yzx.party.dao.LogRepository;
import com.yzx.party.pojo.Logs;
import com.yzx.party.vo.QueryLog;
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
 * @since 2022/2/16 10:37
 */
@Service
public class LogServiceImpl implements LogService{
    @Autowired
    LogRepository logRepository;

    @Transactional
    @Override
    public Page<Logs> listLogs(Pageable pageable, QueryLog queryLog) {
        return logRepository.findAll(new Specification<Logs>() {
            @Override
            public Predicate toPredicate(Root<Logs> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {
                List<Predicate> predicates = new ArrayList<>();
                //根据学院查询
                if (!"".equals(queryLog.getUsercollege()) && queryLog.getUsercollege()!=null){
                    predicates.add(cb.like(root.<String>get("usercollege"),"%"+queryLog.getUsercollege()+"%"));
                }
                //根据类型查询
                if (!"".equals(queryLog.getType()) && queryLog.getType()!=null){
                    predicates.add(cb.like(root.<String>get("type"),"%"+queryLog.getType()+"%"));
                }
                //根据操作者姓名查询
                if (!"".equals(queryLog.getUsercode()) && queryLog.getUsercode()!=null){
                    predicates.add(cb.like(root.<String>get("usercode"),"%"+queryLog.getUsercode()+"%"));
                }
                //根据描述查询
                if (!"".equals(queryLog.getDescription()) && queryLog.getDescription()!=null){
                    predicates.add(cb.like(root.<String>get("description"),"%"+queryLog.getDescription()+"%"));
                }
                cq.where(predicates.toArray(new Predicate[predicates.size()]));
                return null;
            }
        },pageable);
    }

    @Transactional
    @Override
    public Logs saveLogs(Logs logs) {
        return logRepository.save(logs);
    }

    @Transactional
    @Override
    public void deleteLog(Integer id) {
        logRepository.deleteById(id);
    }
}
