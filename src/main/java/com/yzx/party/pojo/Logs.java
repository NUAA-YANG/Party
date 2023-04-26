package com.yzx.party.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

/**
 * @author yzx
 * @version JDK 1.8.0_131
 * @since 2022/2/16 10:17
 */
//日志实体类
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "t_logs")
public class Logs {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 主键自增长
    private Integer id;
    //操作者姓名
    private String usercode;
    //指定操作的类型（查看、搜索、登录、下载、修改、删除、新增）
    private String type;
    private String description;//具体描述
    @Temporal(TemporalType.TIMESTAMP)
    private Date operationtime;//操作时间
    private String usercollege; //操作者所属学院
    private String ipaddress;//访问者的ip地址
}
