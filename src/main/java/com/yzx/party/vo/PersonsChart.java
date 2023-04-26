package com.yzx.party.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author yzx
 * @version JDK 1.8.0_131
 * @since 2022/1/14 10:18
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
//后端的饼状图展示，每个学院的政治面貌饼状图，在学院管理员中展示
public class PersonsChart {
    String appearance;//政治面貌
    Integer numcount;//人数
}
