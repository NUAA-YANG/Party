package com.yzx.party.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author yzx
 * @version JDK 1.8.0_131
 * @since 2022/1/12 9:16
 */
//后台筛选人员
@Data
@AllArgsConstructor
@NoArgsConstructor
public class QueryPerson {
    //姓名
    String name;
    //专业
    Integer major_id;
    //政治面貌
    String appearance;
    //年级
    Integer grade;
}
