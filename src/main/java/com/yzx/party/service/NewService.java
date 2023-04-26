package com.yzx.party.service;

import com.yzx.party.pojo.News;
import com.yzx.party.vo.QueryNew;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * @author yzx
 * @version JDK 1.8.0_131
 * @since 2022/1/6 11:40
 */
//新闻管理
public interface NewService {
    //后端的分页新闻（附带查询）
    Page<News> listNewsByCollegeId(Pageable pageable, QueryNew queryNew);
    //前端的分页新闻（附带查询还有发布状态）
    Page<News> listNews(Pageable pageable, QueryNew queryNew,String published);
    //前端的模糊查询
    Page<News> listNewSearch(String queryWord,Integer college_id ,String published,Pageable pageable);
    //新增新闻
    News saveNews(News news);
    //根据id查询新闻
    News getNewsById(Integer id);
    //修改新闻
    News updateNews(Integer id,News news);
    //更新阅读数量
    News updateViews(Integer id,News news);
    //删除新闻
    void deleteNews(Integer id);
}

