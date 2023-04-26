package com.yzx.party.service;

import java.util.List;
import java.util.Map;

/**
 * @author yzx
 * @version JDK 1.8.0_131
 * @since 2022/3/21 9:58
 */
//用于处理可视化表格
public interface ChartService {
  /*
   * @desc 新闻表格模块数据集合
   * @param 展示多少个学院信息
   * @return 返回的集合顺序：学院名称、新闻发布数量、新闻评论数量、新闻阅读数量
   */
    List<Object> listNewInfo(Integer size);
    /*
     * @desc 党员表格模块数据集合
     * @param
     * @return 返回的集合顺序：学院名称、正式党员人数、预备党员人数、积极分子人数、总人数
     */
    List<Object> listPersonInfo();
    /*
     * @desc 会议表格模块数据集合
     * @param 展示多少个学院信息
     * @return 返回的集合顺序：学院名称、会议总时长
     */
    List<Object> listMeetingInfo(Integer size);
    /*
     * @desc 任务表格模块数据集合
     * @param
     * @return 返回的集合顺序：学院名称、未完成任务数量，完成任务数量
     */
    List<Object> listTaskInfo();


  //新闻评估报告
  // 传入参数表示查看多少个学院情况，发布数量，阅读数量和评论数量分数占比，要求加起来为100
  Map<String,Integer> newEvaluateReport(Integer size, Integer numScore, Integer commentScore, Integer viewScore);


  //计算各部分占总人数的百分比
  // 传入参数表示党员分数，预备党员分数和积极分子分数，要求加起来为100
  void personEvaluateReport(Integer paramOne,Integer paramTwo,Integer paramThree);


  // 传入参数表示会议时长分数
  void meetingEvaluateReport(Integer paramOne);

  //计算完成任务
  // 传入参数表示未完成任务分数，完成任务分数，要求加起来为100
  void taskEvaluateReport(Integer paramOne,Integer paramTwo);


}
