package com.yzx.party.dao;

import com.yzx.party.pojo.Tasks;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @author yzx
 * @version JDK 1.8.0_131
 * @since 2022/3/10 9:56
 */
public interface TaskRepository extends JpaRepository<Tasks,Integer>, JpaSpecificationExecutor<Tasks> {
}
