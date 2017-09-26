package com.bearxyz.repository;

import com.bearxyz.domain.po.business.CommunicationRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

/**
 * Created by bearxyz on 2017/8/28.
 */
public interface CommunicationRecordRepository extends JpaRepository<CommunicationRecord, String>, JpaSpecificationExecutor<CommunicationRecord> {

    Integer countAllByCompanyIdAndSuccessed(String companyId, boolean successed);

}
