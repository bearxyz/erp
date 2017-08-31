package com.bearxyz.repository;

import com.bearxyz.domain.po.business.Contract;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * Created by bearxyz on 2017/7/27.
 */
public interface ContractRepository extends JpaRepository<Contract, String>, JpaSpecificationExecutor<Contract> {
}
