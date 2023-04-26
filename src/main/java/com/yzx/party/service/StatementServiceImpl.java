package com.yzx.party.service;

import com.yzx.party.dao.StatementRepository;
import com.yzx.party.pojo.Statements;
import com.yzx.party.vo.QueryCS;
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
 * @since 2022/2/13 10:23
 */
@Service
public class StatementServiceImpl implements StatementService {
    @Autowired
    StatementRepository statementRepository;

    @Transactional
    @Override
    public Page<Statements> listStatements(Pageable pageable, QueryCS queryCS) {
        return statementRepository.findAll(new Specification<Statements>() {
            @Override
            public Predicate toPredicate(Root<Statements> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {
                List<Predicate> predicates = new ArrayList<>();
                //所属会议标题不为空
                if (!"".equals(queryCS.getTitle()) && queryCS.getTitle()!=null){
                    predicates.add(cb.like(root.<String>get("title"),"%"+queryCS.getTitle()+"%"));
                }
                //评论内容的模糊查询
                if (!"".equals(queryCS.getContent()) && queryCS.getContent()!=null){
                    predicates.add(cb.like(root.<String>get("content"),"%"+queryCS.getContent()+"%"));
                }
                //所属学院
                if (queryCS.getCollege_id()!=null){
                    predicates.add(cb.equal(root.get("college_id"),queryCS.getCollege_id()));
                }
                cq.where(predicates.toArray(new Predicate[predicates.size()]));
                return null;
            }
        },pageable);
    }

    @Transactional
    @Override
    public List<Statements> listStatements(Integer meeting_id) {
        return statementRepository.findByMeeting_id(meeting_id);
    }

    @Transactional
    @Override
    public Statements saveStatement(Statements statements) {
        return statementRepository.save(statements);
    }

    @Transactional
    @Override
    public void deleteStatements(Integer id) {
        statementRepository.deleteById(id);
    }
}
