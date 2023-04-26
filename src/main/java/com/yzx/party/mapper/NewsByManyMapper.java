package com.yzx.party.mapper;

import com.yzx.party.vo.NewsByMany;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author yzx
 * @version JDK 1.8.0_131
 * @since 2022/1/16 11:50
 */
//这个注解表示了这是一个Mybaits的mapper类
@Mapper
//加入仓库管理才能使用
@Repository
public interface NewsByManyMapper {
    //前端展示最新发布
    List<NewsByMany> listNewsByUpdateTime(Integer college_id, String published, Integer size);

    //前端展示阅读数量最多的文章
    List<NewsByMany> listNewsByViews(Integer college_id, String published, Integer size);

    //前端展示评论数量最多的文章
    List<NewsByMany> listNewsByComments(Integer college_id, String published, Integer size);

}
