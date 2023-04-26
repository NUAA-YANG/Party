package com.yzx.party.dao;

import com.yzx.party.pojo.Forgets;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @author yzx
 * @version JDK 1.8.0_131
 * @since 2022/3/14 11:12
 */
public interface ForgetRepository extends JpaRepository<Forgets,Integer>, JpaSpecificationExecutor<Forgets> {
    Forgets findByUsernameAndEmail(String username,String email);
}
