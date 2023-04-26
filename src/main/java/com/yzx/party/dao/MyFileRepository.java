package com.yzx.party.dao;

import com.yzx.party.pojo.MyFiles;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @author yzx
 * @version JDK 1.8.0_131
 * @since 2022/1/27 10:35
 */
public interface MyFileRepository extends JpaRepository<MyFiles,Integer>, JpaSpecificationExecutor<MyFiles> {
    MyFiles findByTitle(String title);
}
