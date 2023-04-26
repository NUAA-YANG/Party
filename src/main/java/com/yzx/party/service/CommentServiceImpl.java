package com.yzx.party.service;

import com.yzx.party.dao.CommentRepository;
import com.yzx.party.pojo.Comments;
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
 * @since 2022/1/18 11:47
 */
@Service
public class CommentServiceImpl implements CommentService {
    @Autowired
    CommentRepository commentRepository;

    @Transactional
    @Override
    public Page<Comments> listComments(Pageable pageable, QueryCS queryCS) {
        return commentRepository.findAll(new Specification<Comments>() {
            @Override
            public Predicate toPredicate(Root<Comments> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {
                List<Predicate> predicates = new ArrayList<>();
                //所属新闻标题不为空
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
    public List<Comments> listCommentsParent(Integer new_id) {
        return commentRepository.findByNew_idAndParent_idIsNull(new_id);
    }

    @Transactional
    @Override
    public List<Comments> listCommentsSon(Integer new_id) {
        return commentRepository.findByNew_idAndParent_idIsNotNull(new_id);
    }

    @Transactional
    @Override
    public Comments getCommentById(Integer id) {
        return commentRepository.findById(id).get();
    }

    @Transactional
    @Override
    public Comments saveComment(Comments comments) {
        return commentRepository.save(comments);
    }

    @Transactional
    @Override
    public void deleteCommentById(Integer id) {
        //删除父评论
        commentRepository.deleteById(id);

    }

//    @Override
//    public void deleteCommentByParent_id(Integer parent_id) {
//        //删除子级评论
//        commentRepository.deleteByParent_id(parent_id);
//    }


}
