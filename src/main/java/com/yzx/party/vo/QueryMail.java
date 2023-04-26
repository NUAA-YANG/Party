package com.yzx.party.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author yzx
 * @version JDK 1.8.0_131
 * @since 2022/2/18 11:16
 */
//后端模糊查询书记信箱
//前端展示给用户
@Data
@AllArgsConstructor
@NoArgsConstructor
public class QueryMail {
    //后端管理员选择信件类型(投诉、举报、求助、咨询、建议、其他)
    private String qtype;
    //所属学院
    private Integer college_id;
    //展示给后台管理员的状态（未读、已读、已回复）
    private String state;
    //发布者id(这个是用于前端展示给用户)
    private Integer person_id;
    //展示给前台的状态（未回复和已回复）
    private String statetwo;


}
