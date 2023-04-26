package com.yzx.party.service;

import com.yzx.party.dao.NewRepository;
import com.yzx.party.myException.NotFoundException;
import com.yzx.party.pojo.News;
import com.yzx.party.vo.QueryNew;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
 * @since 2022/1/6 11:52
 */
@Service
public class NewServiceImpl implements NewService {
    @Autowired
    NewRepository newRepository;

    @Transactional
    //后端的超级管理员的模糊查询
    @Override
    public Page<News> listNewsByCollegeId(Pageable pageable, QueryNew queryNew) {
        return newRepository.findAll(new Specification<News>() {
            //cq用于设置查询的大小，cb用于设置模糊查询的参数
            @Override
            public Predicate toPredicate(Root<News> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {
                List<Predicate> predicates = new ArrayList<>();
                //标题的模糊查询
                if (!"".equals(queryNew.getTitle()) && queryNew.getTitle()!=null ){
                    predicates.add(cb.like(root.<String>get("title"),"%"+queryNew.getTitle()+"%"));
                }
                //学院的模糊查询
                if (queryNew.getCollege_id()!=null){
                    predicates.add(cb.equal(root.<Integer>get("college_id"),queryNew.getCollege_id()));
                }
                //专业的模糊查询
                if (queryNew.getMajor_id()!=null){
                    predicates.add(cb.equal(root.<Integer>get("major_id"),queryNew.getMajor_id()));
                }
                cq.where(predicates.toArray(new Predicate[predicates.size()]));
                return null;
            }
        },pageable);
    }

    //后端的学院管理员模糊查询
    @Transactional
    @Override
    public Page<News> listNews(Pageable pageable, QueryNew queryNew,String published) {
        return newRepository.findAll(new Specification<News>() {
            //cq用于设置查询的大小，cb用于设置模糊查询的参数
            @Override
            public Predicate toPredicate(Root<News> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {
                List<Predicate> predicates = new ArrayList<>();
                //标题的模糊查询
                if (!"".equals(queryNew.getTitle()) && queryNew.getTitle()!=null ){
                    predicates.add(cb.like(root.<String>get("title"),"%"+queryNew.getTitle()+"%"));
                }
                //学院的模糊查询
                if (queryNew.getCollege_id()!=null){
                    predicates.add(cb.equal(root.<Integer>get("college_id"),queryNew.getCollege_id()));
                }
                //专业的模糊查询
                if (queryNew.getMajor_id()!=null){
                    predicates.add(cb.equal(root.<Integer>get("major_id"),queryNew.getMajor_id()));
                }
                //发布状态的查询，草稿或者发布
                if (published!=null){
                    predicates.add(cb.equal(root.<Integer>get("published"),published));
                }
                cq.where(predicates.toArray(new Predicate[predicates.size()]));
                return null;
            }
        },pageable);
    }


    //前端的搜索
    @Override
    public Page<News> listNewSearch(String queryWord, Integer college_id,  String published,Pageable pageable) {
        return newRepository.findNewsSearch(queryWord, college_id, published,pageable);
    }

    @Transactional
    @Override
    public News saveNews(News news) {
        return newRepository.save(news);
    }

    @Transactional
    @Override
    public News getNewsById(Integer id) {
        return newRepository.findById(id).get();
    }

    @Transactional
    @Override
    public News updateNews(Integer id, News news) {
        News n = newRepository.findById(id).get();
        if (n==null){
            throw new NotFoundException("不存在该类型");
        }
        //复制一个对象
        BeanUtils.copyProperties(news,n);
        //因为存在该id，所以再次储存的时候就相当于更新了数据
        return newRepository.save(n);
    }

    @Transactional
    @Override
    public News updateViews(Integer id,News news) {
        News n = newRepository.findById(id).get();
        if (n==null){
            throw new NotFoundException("不存在该类型");
        }
        //复制一个对象
        BeanUtils.copyProperties(news,n);
        n.setViews(n.getViews()+1);
        return newRepository.save(n);
    }

    @Transactional
    @Override
    public void deleteNews(Integer id) {
        newRepository.deleteById(id);
    }
}
