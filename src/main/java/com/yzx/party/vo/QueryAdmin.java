package com.yzx.party.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author yzx
 * @version JDK 1.8.0_131
 * @since 2022/1/8 11:33
 */
//后端查询管理员
@Data
@AllArgsConstructor
@NoArgsConstructor
public class QueryAdmin {
    String name;
    Integer college_id;
    Integer major_id;
}
