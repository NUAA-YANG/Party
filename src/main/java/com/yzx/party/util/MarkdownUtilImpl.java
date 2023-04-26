package com.yzx.party.util;


import com.yzx.party.myException.NotFoundException;
import com.yzx.party.pojo.News;
import com.yzx.party.service.NewService;
import org.springframework.beans.BeanUtils;

/**
 * @author yzx
 * @version JDK 1.8.0_131
 * @since 2022/1/7 9:26
 */
//新闻详情页面将markdown转化为html
public class MarkdownUtilImpl {
    NewService newService;
    public News getAndConvert(Integer id){
        News news = newService.getNewsById(id);
        if (news==null){
            throw new NotFoundException("该博客不存在");
        }
        News n = new News();
        //将对象blog复制给b，那么后面的属性修改就不会修改数据库中的值
        BeanUtils.copyProperties(news,n);
        String content = n.getContent();
        n.setContent(new MarkdownUtil().markdownToHtmlExtensions(content));
        return n;
    }


}
