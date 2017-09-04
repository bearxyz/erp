package com.bearxyz.repository;

import com.bearxyz.domain.po.business.SupportApply;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Created by bearxyz on 2017/9/4.
 */
public interface SupportApplyRepository extends JpaRepository<SupportApply, String>, JpaSpecificationExecutor<SupportApply> {

    @Query(value = "SELECT s.* FROM T_SUPPORT_APPLY s " +
            "INNER JOIN T_COMPANY c ON c.ID = s.COMPANY_ID " +
            "INNER JOIN T_USER_PROVINCE cp on cp.PROVINCE = c.PROVINCE " +
            "WHERE cp.USER_ID=?1", nativeQuery = true)
    List<SupportApply> getAllByClientId(String cid);

}
