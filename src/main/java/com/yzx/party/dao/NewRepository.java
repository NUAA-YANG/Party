package com.yzx.party.dao;

import com.yzx.party.pojo.News;
import com.yzx.party.vo.QueryNew;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * @author yzx
 * @version JDK 1.8.0_131
 * @since 2022/1/6 11:54
 */
public interface NewRepository extends JpaRepository<News,Integer>, JpaSpecificationExecutor<News> {

    //模糊查询，只根据新闻的题目和简介在学校新闻（n.college_id=1）和学院新闻（n.college_id=?2）中模糊查询
    @Query("select n from News n where (n.title like ?1 or n.introduce like ?1) and (n.college_id=?2 or n.college_id=1) and n.published=?3")
    Page<News> findNewsSearch(String queryWord ,Integer college_id,String published,Pageable pageable);

    @Query("select n from News n where n.college_id=?1 order by n.updatetime desc ")
    List<News> findByCollege_id(Integer college_id);




}
