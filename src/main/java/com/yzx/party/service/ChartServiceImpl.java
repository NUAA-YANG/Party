package com.yzx.party.service;

import com.yzx.party.mapper.MeetingChartMapper;
import com.yzx.party.mapper.NewsChartTwoMapper;
import com.yzx.party.mapper.PersonsTableMapper;
import com.yzx.party.pojo.Colleges;
import com.yzx.party.pojo.Tasks;
import com.yzx.party.util.TaskUtil;
import com.yzx.party.vo.MeetingChart;
import com.yzx.party.vo.NewsChartTwo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;

/**
 * @author yzx
 * @version JDK 1.8.0_131
 * @since 2022/3/21 10:18
 */
@Service
public class ChartServiceImpl implements ChartService {

    @Autowired
    CollegeService collegeService;
    @Autowired
    NewsChartTwoMapper newsChartTwoMapper;
    @Autowired
    PersonsTableMapper personsTableMapper;
    @Autowired
    MeetingChartMapper meetingChartMapper;
    @Autowired
    TaskService taskService;
    @Autowired
    ChartService chartService;


    @Transactional
    @Override
    public List<Object> listNewInfo(Integer size) {
        //查询前七个学院的新闻情况
        List<NewsChartTwo> newsChartTwos = newsChartTwoMapper.radarNews(7);
        //学院名称
        List<String> listName = new ArrayList<>();
        //新闻发布数量
        List<Integer> listPublicNum = new ArrayList<>();
        //新闻评论数量
        List<Integer> listCommentNum = new ArrayList<>();
        //新闻阅读数量
        List<Integer> listViewNum= new ArrayList<>();
        for (NewsChartTwo nct:newsChartTwos){
            listName.add(nct.getName());
            listPublicNum.add(nct.getNums());
            listCommentNum.add(nct.getCommentsNum());
            listViewNum.add(nct.getViewsNum());
        }
        List<Object> listResult = new ArrayList<>();
        listResult.add(listName);
        listResult.add(listPublicNum);
        listResult.add(listCommentNum);
        listResult.add(listViewNum);
        return listResult;
    }

    @Transactional
    @Override
    public List<Object> listPersonInfo() {
        //数据库查询的数据
        List<Integer> listCollege_id = personsTableMapper.listCollege_id();
        //封装学院名称
        List<String> listName = new ArrayList<>();
        //正式党员人数
        List<Integer> listOff = new ArrayList<>();
        //预备党员人数
        List<Integer> listPre = new ArrayList<>();
        //积极分子人数
        List<Integer> listPos = new ArrayList<>();
        //总人数
        List<Integer> listSum = new ArrayList<>();
        for (int i =0 ;i<listCollege_id.size();i++){
            Integer college_id = listCollege_id.get(i);
            String collegeName = collegeService.getCollegeById(college_id).getName();
            listName.add(collegeName);
            if (personsTableMapper.findPersonsTable(college_id,"officialM")!=null){
                Integer countOff = personsTableMapper.findPersonsTable(college_id, "officialM").getCountapp();
                listOff.add(countOff);
            }else {
                listOff.add(0);
            }
            if (personsTableMapper.findPersonsTable(college_id,"prepareM")!=null){
                Integer countPre = personsTableMapper.findPersonsTable(college_id, "prepareM").getCountapp();
                listPre.add(countPre);
            }else {
                listPre.add(0);
            }
            if (personsTableMapper.findPersonsTable(college_id,"positiveM")!=null){
                Integer countPos = personsTableMapper.findPersonsTable(college_id, "positiveM").getCountapp();
                listPos.add(countPos);
            }else {
                listPos.add(0);
            }
            listSum.add(listPre.get(i)+listOff.get(i)+listPos.get(i));
        }
        List<Object> listResult = new ArrayList<>();
        listResult.add(listName);
        listResult.add(listOff);
        listResult.add(listPre);
        listResult.add(listPos);
        listResult.add(listSum);
        return listResult;
    }

    @Transactional
    @Override
    public List<Object> listMeetingInfo(Integer size) {
        //获取三会一课时长最多的前七个学院
        List<MeetingChart> allTopMeetings = meetingChartMapper.findAllTop(20);
        //将学院名字和发布新闻的数量取出
        List<String> listName = new ArrayList<>();
        List<Double> listTimes= new ArrayList<>();
        for (MeetingChart meetingChart:allTopMeetings){
            listName.add(meetingChart.getName());
            listTimes.add(meetingChart.getSumtime());
        }
        List<Object> listResult = new ArrayList<>();
        listResult.add(listName);
        listResult.add(listTimes);
        return listResult;
    }

    @Transactional
    @Override
    public List<Object> listTaskInfo() {
        //获得全部任务
        List<Tasks> listTask = taskService.listTask();
        //任务处理工具
        TaskUtil taskUtil = new TaskUtil();
        //获得全部学院
        List<Colleges> colleges = collegeService.listColleges();
        List<Integer> finishedTaskList = new ArrayList<>();
        List<Integer> unFinishTaskList = new ArrayList<>();
        List<String> taskCollegeName = new ArrayList<>();
        //遍历所有的学院完成情况
        for (Colleges college:colleges){
            Integer id = college.getId();
            //每个学院处理过后的完成任务和未完成任务
            //第一个元素未完成任务，第二个元素已完成任务
            List<List<Tasks>> taskUtilHelperList = taskUtil.TaskUtilHelper(listTask, id);
            //添加学院名称
            taskCollegeName.add(college.getName());
            //获得未完成任务的数量
            unFinishTaskList.add(taskUtilHelperList.get(1).size());
            //获得已完成任务的数量
            finishedTaskList.add(taskUtilHelperList.get(0).size());
        }
        List<Object> listResult = new ArrayList<>();
        listResult.add(taskCollegeName);
        listResult.add(unFinishTaskList);
        listResult.add(finishedTaskList);
        return listResult;
    }



    @Override
    public Map<String,Integer> newEvaluateReport(Integer size,Integer numScore, Integer commentScore, Integer viewScore) {
        //查询前size个学院的新闻情况
        List<NewsChartTwo> newsChartTwos = newsChartTwoMapper.radarNews(size);
        Map<String,Integer> mapNew = new TreeMap<>();
        for (NewsChartTwo nct:newsChartTwos){
            Integer nums = nct.getNums();
            Integer numTwo = nct.getCommentsNum() / nums;
            Integer numThree = nct.getViewsNum() / nums;
            //自定义规则，发布数量多少分一篇，（评论量/数量）多少分，（浏览量/数量）多少分
           mapNew.put( nct.getName(), nums*numScore + numTwo*commentScore + numThree*viewScore);
        }
        //根据排序
        ArrayList<Map.Entry<String, Integer>> list = new ArrayList<>(mapNew.entrySet());
        Collections.sort(list,((o1, o2) -> {
            return -(o1.getValue().compareTo(o2.getValue()));
        }));
        //用迭代器对list中的元素遍历
        Iterator<Map.Entry<String, Integer>> iterator = list.iterator();
        Map<String,Integer> result = new TreeMap<>();
        while (iterator.hasNext()) {
            Map.Entry<String, Integer> entry = iterator.next();
            String key = entry.getKey();
            int value = entry.getValue();
            result.put(key,value);
        }
        list.forEach(x-> System.out.println(x));
        return result;
    }

    @Override
    public void personEvaluateReport(Integer paramOne, Integer paramTwo, Integer paramThree) {

    }

    @Override
    public void meetingEvaluateReport(Integer paramOne) {

    }

    @Override
    public void taskEvaluateReport(Integer paramOne, Integer paramTwo) {

    }


}
