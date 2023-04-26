package com.yzx.party.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author yzx
 * @version JDK 1.8.0_131
 * @since 2022/2/16 10:34
 */
//后端的日志管理
@Data
@AllArgsConstructor
@NoArgsConstructor
public class QueryLog {
    //操作类型（增删改查）
    private String type;
    //操作者姓名
    private String usercode;
    //具体描述
    private String description;
    //操作者所属学院
    private String usercollege;
}
