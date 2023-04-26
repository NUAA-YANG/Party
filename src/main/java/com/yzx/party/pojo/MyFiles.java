package com.yzx.party.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

/**
 * @author yzx
 * @version JDK 1.8.0_131
 * @since 2022/1/27 9:42
 */
//文件实体类
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "t_myfiles")
public class MyFiles {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 主键自增长
    private Integer id;
    private Date uploadtime;//文件的上传时间
    private String title;//上传的文件名
    private Integer person_id;//上传人员的id
    private Integer college_id;//所属学院id
    private String person_name;//上传人员名字
    private Integer major_id;//专业id
}
