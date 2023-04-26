package com.yzx.party.dao;

import com.yzx.party.pojo.Mailboxs;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @author yzx
 * @version JDK 1.8.0_131
 * @since 2022/2/18 10:43
 */
public interface MailboxRepository extends JpaRepository<Mailboxs,Integer>, JpaSpecificationExecutor<Mailboxs> {
}
