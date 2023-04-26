package com.yzx.party.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author yzx
 * @version JDK 1.8.0_131
 * @since 2022/2/15 10:45
 */
//后端折线图展示三会一课时长
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MeetingChart {
    private Integer id;//学院id
    private String name;//学院名称
    private Double sumtime;//持续总时长
}


