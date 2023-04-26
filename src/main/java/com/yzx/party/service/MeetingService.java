package com.yzx.party.service;

import com.yzx.party.pojo.Meetings;
import com.yzx.party.vo.QueryMeeting;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * @author yzx
 * @version JDK 1.8.0_131
 * @since 2022/2/7 17:17
 */
public interface MeetingService {
    //后端和前端查询分页
    Page<Meetings> listMeetings(Pageable pageable, QueryMeeting queryMeeting);
    //新增
    Meetings saveMeeting(Meetings meetings);
    //根据条件查询
    Meetings getMeetingById(Integer id);
    //修改
    Meetings updateMeetings(Integer id,Meetings meetings);
    //删除
    void deleteMeetings(Integer id);
}
