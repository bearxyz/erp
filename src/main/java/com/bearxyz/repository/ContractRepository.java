package com.bearxyz.repository;

import com.bearxyz.domain.po.business.Contract;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Created by bearxyz on 2017/7/27.
 */
public interface ContractRepository extends JpaRepository<Contract, String>, JpaSpecificationExecutor<Contract> {

    @Query(value = "select * from (select * from T_CONTRACT where COMPANY_ID=?1 order by END_DATE DESC) where ROWNUM<=1", nativeQuery = true)
    Contract findNewestContractByCompanyId(String companyId);

    @Query(value = "SELECT  * FROM T_CONTRACT WHERE EXPIRED!=1 AND APPROVED = 1 AND INVALID != 1 AND COMPANY_ID=?1", nativeQuery = true)
    List<Contract> findAvaliableContractByCompanyId(String companyId);

    List<Contract> findAllBySecond(boolean isSec);

}
