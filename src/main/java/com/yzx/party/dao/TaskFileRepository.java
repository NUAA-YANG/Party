package com.yzx.party.dao;

import com.yzx.party.pojo.TaskFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @author yzx
 * @version JDK 1.8.0_131
 * @since 2022/3/11 10:11
 */
public interface TaskFileRepository extends JpaRepository<TaskFile,Integer>, JpaSpecificationExecutor<TaskFile> {
    TaskFile findByFile(String fileName);
}
