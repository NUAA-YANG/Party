package com.yzx.party.service;

import com.yzx.party.pojo.Mailboxs;
import com.yzx.party.vo.QueryMail;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * @author yzx
 * @version JDK 1.8.0_131
 * @since 2022/2/18 10:49
 */
//书记信箱
public interface MailBoxService {
    //前端展示
    Page<Mailboxs> listMail(Pageable pageable, QueryMail queryMail);
    //后端展示
    Page<Mailboxs> listMailCollege(Pageable pageable, QueryMail queryMail);
    //新增
    Mailboxs saveMail(Mailboxs mailboxs);
    //查询
    Mailboxs getMailById(Integer id);
    //修改
    Mailboxs updateMail(Integer id,Mailboxs mailboxs);
    //删除
    void deleteMail(Integer id);
}
