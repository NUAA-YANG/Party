package com.yzx.party.util;

import com.yzx.party.pojo.Tasks;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author yzx
 * @version JDK 1.8.0_131
 * @since 2022/3/12 15:33
 */
//用于任务处理，输出一个集合，分别代表未完成和已完成
public class TaskUtil {
    /*
     * @return 集合第一个元素未完成任务，第二个元素已完成任务
     * @parm 第一个参数为全部的任务集合，第二个参数为本学院id
     */
    public List<List<Tasks>> TaskUtilHelper(List<Tasks> tasksList,Integer college_id){
        //针对所有的任务，区分出未完成和已完成
        //遍历所有的任务，切分已完成学院的id，如果没有本管理员所在学院id，则是未完成；否则为已完成
        List<Tasks> finishedTask = new ArrayList<>();
        List<Tasks> unFinishedTask = new ArrayList<>();
        List<List<Tasks>> result = new ArrayList<>();
        for (Tasks tasks : tasksList){
            String compcollege_id = tasks.getCompcollege_id();
            //如果存在数据，就切分，并且使用","切分已完成学院id
            if (compcollege_id!=null){
                String[] splitCollege_id = compcollege_id.split(",");
                List<String> listSplitCollege_id = Arrays.asList(splitCollege_id); //数组转化为list
                //如果包含本学院id，则此任务已完成
                if (listSplitCollege_id.contains(String.valueOf(college_id))){
                    finishedTask.add(tasks);
                }else {
                    unFinishedTask.add(tasks);
                }
            }else {
                unFinishedTask.add(tasks);//如果不存在数据，那么肯定是未完成
            }
        }
        //第一个元素未完成任务
        result.add(unFinishedTask);
        //第二个元素已完成任务
        result.add(finishedTask);
        return result;
    }
}
