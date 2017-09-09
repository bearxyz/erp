package com.bearxyz.repository;

import com.bearxyz.domain.po.business.WorkOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Created by bearxyz on 2017/9/6.
 */
public interface WorkOrderRepository extends JpaRepository<WorkOrder, String>, JpaSpecificationExecutor<WorkOrder> {

    List<WorkOrder> findAllByCompanyId(String companyId);

    @Query(value = "SELECT s.* FROM T_WORK_ORDER s " +
            "INNER JOIN T_COMPANY c ON c.ID = s.COMPANY_ID " +
            "INNER JOIN T_USER_PROVINCE cp on cp.PROVINCE = c.PROVINCE " +
            "WHERE cp.USER_ID=?1", nativeQuery = true)
    List<WorkOrder> findAllByClientId(String clientId);

    Integer countAllByCompanyIdAndFinished(String companyId, boolean finished);

}
