package com.yzx.party.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author yzx
 * @version JDK 1.8.0_131
 * @since 2022/1/27 11:07
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
//后端文件的模糊查询
public class QueryFile {
    //模糊匹配文件名或者上传人员姓名
    String title;
    String person_name;
    //学院id
    Integer college_id;
    //专业id
    Integer major_id;

}
