package com.yzx.party.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author yzx
 * @version JDK 1.8.0_131
 * @since 2022/2/7 17:18
 */
//查询会议
@Data
@AllArgsConstructor
@NoArgsConstructor
public class QueryMeeting {
    //标题
    String title;
    //类型
    private String mtype;
    //会议所属学院
    private Integer college_id;
}
