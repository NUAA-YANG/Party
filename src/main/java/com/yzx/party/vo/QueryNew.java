package com.yzx.party.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author yzx
 * @version JDK 1.8.0_131
 * @since 2022/1/6 12:09
 */
//查询新闻板块
@Data
@AllArgsConstructor
@NoArgsConstructor
public class QueryNew {
    //标题
    String title;
    //学院id
    Integer college_id;
    //专业id
    Integer major_id;
}
