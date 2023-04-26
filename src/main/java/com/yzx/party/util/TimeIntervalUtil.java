package com.yzx.party.util;

import java.text.DecimalFormat;
import java.util.Date;

/**
 * @author yzx
 * @version JDK 1.8.0_131
 * @since 2022/2/15 9:56
 */
//计算两个日期之间间隔多少个小时的工具类
public class TimeIntervalUtil {
    public Double TimeIntervalUtilHelper(Date d1,Date d2){
        //如果开始时间大于结束时间
        if (d1.after(d2)){
            return 0.0;
        }
        //得到的两个时间差差是毫秒级别
        long diff = d2.getTime() - d1.getTime();
        //多少天
        long days = diff / (1000 * 60 * 60 * 24);
        //多少小时
        long hours = (diff-days*(1000 * 60 * 60 * 24))/(1000* 60 * 60);
        //多少分钟
        long minutes = (diff-days*(1000 * 60 * 60 * 24)-hours*(1000* 60 * 60))/(1000* 60);
        //格式化时间，使之保留一位小数（四舍五入）
        DecimalFormat df = new DecimalFormat("0.0");
        //总的间隔多少小时
        double result = days*24+hours+(double)minutes/60;
        return Double.parseDouble(df.format(result));
    }
}
