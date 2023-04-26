package com.yzx.party.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author yzx
 * @version JDK 1.8.0_131
 * @since 2022/2/13 10:26
 */
//后端的评论和发言管理
@Data
@AllArgsConstructor
@NoArgsConstructor
public class QueryCS {
    //本身发言的id
    Integer id;
    //内容
    String content;
    //所属学院
    Integer college_id;
    //会议或者新闻标题
    String title;

}
