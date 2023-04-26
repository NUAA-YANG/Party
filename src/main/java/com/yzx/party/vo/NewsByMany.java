package com.yzx.party.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author yzx
 * @version JDK 1.8.0_131
 * @since 2022/1/16 11:49
 */
//前端获取的阅读数量最多;评论数量最多
@Data
@AllArgsConstructor
@NoArgsConstructor
public class NewsByMany {
    //博客id
    Integer id;
    //博客题目
    String title;
}
