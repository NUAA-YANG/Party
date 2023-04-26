package com.yzx.party.service;

import com.yzx.party.pojo.Comments;
import com.yzx.party.vo.QueryCS;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * @author yzx
 * @version JDK 1.8.0_131
 * @since 2022/1/18 11:42
 */
public interface CommentService {
    //展示某个新闻下的所有评论
    Page<Comments> listComments(Pageable pageable, QueryCS queryCS);
    //展示所有的父级评论
    List<Comments> listCommentsParent(Integer new_id);
    //展示所有的子级评论
    List<Comments> listCommentsSon(Integer new_id);
    //根据id获得评论
    Comments getCommentById(Integer id);
    //新增
    Comments saveComment(Comments comments);
    //根据id删除（父级评论）
    void deleteCommentById(Integer id);
}
