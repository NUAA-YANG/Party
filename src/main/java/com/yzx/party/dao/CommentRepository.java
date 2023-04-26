package com.yzx.party.dao;

import com.yzx.party.pojo.Comments;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * @author yzx
 * @version JDK 1.8.0_131
 * @since 2022/1/18 11:41
 */

//新闻评论
public interface CommentRepository extends JpaRepository<Comments,Integer>, JpaSpecificationExecutor<Comments> {

    //根据新闻id且父级评论不为空的新闻（即获取所有子评论）
    @Query("select c from Comments c where c.new_id=?1 and c.parent_id is not null order by c.creattime desc ")
    List<Comments> findByNew_idAndParent_idIsNotNull(Integer new_id);

    //根据新闻id且父级评论为空的新闻（即获取所有顶级的评论）
    @Query("select c from Comments c where c.new_id=?1 and c.parent_id is null order by c.creattime desc ")
    List<Comments> findByNew_idAndParent_idIsNull(Integer new_id);

}
