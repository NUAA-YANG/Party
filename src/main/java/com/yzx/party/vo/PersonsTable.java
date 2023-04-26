package com.yzx.party.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author yzx
 * @version JDK 1.8.0_131
 * @since 2022/1/15 10:00
 */
//后端超级管理员展示人员分布数据，这个是从数据库查询的数据，封装到personshow
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PersonsTable {
    Integer id;//对应的学院id
    String name;//学院的名字
    String appearance;//政治面貌
    Integer countapp;//人数
}


