package com.yzx.party.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author yzx
 * @version JDK 1.8.0_131
 * @since 2022/3/10 10:11
 */
//后端查询任务
@Data
@AllArgsConstructor
@NoArgsConstructor
public class QueryTask {
    private Integer college_id;
    private String title;
}
