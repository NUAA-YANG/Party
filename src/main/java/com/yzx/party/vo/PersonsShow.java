package com.yzx.party.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author yzx
 * @version JDK 1.8.0_131
 * @since 2022/1/15 11:11
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
//后端构建人员政治面貌展示，对应了personstable，在超级管理员中展示
public class PersonsShow {
    Integer id;
    String name;
    Integer countOff;
    Integer countPre;
    Integer countPos;
}
