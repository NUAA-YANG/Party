package com.yzx.party.service;

import com.yzx.party.dao.MeetingRepository;
import com.yzx.party.myException.NotFoundException;
import com.yzx.party.pojo.Meetings;
import com.yzx.party.vo.QueryMeeting;
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
 * @since 2022/2/7 17:22
 */
@Service
public class MeetingServiceImpl implements MeetingService {

    @Autowired
    MeetingRepository meetingRepository;

    @Transactional
    @Override
    public Page<Meetings> listMeetings(Pageable pageable, QueryMeeting queryMeeting) {
        return meetingRepository.findAll(new Specification<Meetings>() {
            @Override
            public Predicate toPredicate(Root<Meetings> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {
                List<Predicate> predicates = new ArrayList<>();
                //标题的模糊查询
                if (!"".equals(queryMeeting.getTitle()) && queryMeeting.getTitle()!=null ){
                    predicates.add(cb.like(root.<String>get("title"),"%"+queryMeeting.getTitle()+"%"));
                }
                //类型的模糊查询
                if (!"".equals(queryMeeting.getMtype()) && queryMeeting.getMtype()!=null){
                    predicates.add(cb.like(root.<String>get("mtype"),"%"+queryMeeting.getMtype()+"%"));
                }
                //学院的模糊查询
                if (queryMeeting.getCollege_id()!=null){
                    predicates.add(cb.equal(root.<Integer>get("college_id"),queryMeeting.getCollege_id()));
                }
                cq.where(predicates.toArray(new Predicate[predicates.size()]));
                return null;
            }
        },pageable);
    }



    @Transactional
    @Override
    public Meetings saveMeeting(Meetings meetings) {
        return meetingRepository.save(meetings);
    }

    @Transactional
    @Override
    public Meetings getMeetingById(Integer id) {
        return meetingRepository.findById(id).get();
    }

    @Transactional
    @Override
    public Meetings updateMeetings(Integer id, Meetings meetings) {
        Meetings m = meetingRepository.findById(id).get();
        if (m==null){
            throw new NotFoundException("不存在该类型");
        }
        BeanUtils.copyProperties(meetings,m);
        return meetingRepository.save(m);
    }

    @Transactional
    @Override
    public void deleteMeetings(Integer id) {
        meetingRepository.deleteById(id);
    }
}
