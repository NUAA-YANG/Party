package com.yzx.party.service;

import com.yzx.party.dao.MailboxRepository;
import com.yzx.party.myException.NotFoundException;
import com.yzx.party.pojo.Mailboxs;
import com.yzx.party.vo.QueryMail;
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
 * @since 2022/2/18 11:51
 */
@Service
public class MailBoxServiceImpl implements MailBoxService {

    @Autowired
    MailboxRepository mailboxRepository;

    @Transactional
    @Override
    public Page<Mailboxs> listMail(Pageable pageable, QueryMail queryMail) {
        return mailboxRepository.findAll(new Specification<Mailboxs>() {
            @Override
            public Predicate toPredicate(Root<Mailboxs> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {
                List<Predicate> predicates = new ArrayList<>();
                //所属人员
                if (queryMail.getPerson_id()!=null){
                    predicates.add(cb.equal(root.get("person_id"),queryMail.getPerson_id()));
                }
                //未回复或已回复
                if (!"".equals(queryMail.getStatetwo()) && queryMail.getStatetwo()!=null){
                    predicates.add(cb.like(root.<String>get("statetwo"),"%"+queryMail.getStatetwo()+"%"));
                }
                cq.where(predicates.toArray(new Predicate[predicates.size()]));
                return null;
            }
        },pageable);
    }


    @Override
    public Page<Mailboxs> listMailCollege(Pageable pageable, QueryMail queryMail) {
        return mailboxRepository.findAll(new Specification<Mailboxs>() {
            @Override
            public Predicate toPredicate(Root<Mailboxs> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {
                List<Predicate> predicates = new ArrayList<>();
                //所属学院
                if (queryMail.getCollege_id()!=null){
                    predicates.add(cb.equal(root.<Integer>get("college_id"),queryMail.getCollege_id()));
                }
                //所属类型
                if (!"".equals(queryMail.getQtype()) && queryMail.getQtype()!=null){
                    predicates.add(cb.like(root.<String>get("qtype"),"%"+queryMail.getQtype()+"%"));
                }
                //状态一
                if (!"".equals(queryMail.getState()) && queryMail.getState()!=null){
                    predicates.add(cb.like(root.<String>get("state"),"%"+queryMail.getState()+"%"));
                }
                cq.where(predicates.toArray(new Predicate[predicates.size()]));
                return null;
            }
        },pageable);
    }


    @Transactional
    @Override
    public Mailboxs saveMail(Mailboxs mailboxs) {
        return mailboxRepository.save(mailboxs);
    }

    @Transactional
    @Override
    public Mailboxs getMailById(Integer id) {
        return mailboxRepository.findById(id).get();
    }

    @Transactional
    @Override
    public Mailboxs updateMail(Integer id, Mailboxs mailboxs) {
        Mailboxs m = mailboxRepository.findById(id).get();
        if (m==null){
            throw new NotFoundException("不存在该类型");
        }
        BeanUtils.copyProperties(mailboxs,m);
        return mailboxRepository.save(m);
    }

    @Transactional
    @Override
    public void deleteMail(Integer id) {
        mailboxRepository.deleteById(id);
    }
}
