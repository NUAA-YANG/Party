package com.yzx.party.dao;

import com.yzx.party.pojo.Logs;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

/**
 * @author yzx
 * @version JDK 1.8.0_131
 * @since 2022/2/16 10:32
 */
public interface LogRepository extends JpaRepository<Logs,Integer>, JpaSpecificationExecutor<Logs> {

}
