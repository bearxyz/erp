package com.bearxyz.repository;

import com.bearxyz.domain.po.business.OfficialPartner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

/**
 * Created by bearxyz on 2017/7/24.
 */
public interface OfficialPartnerRepository extends JpaRepository<OfficialPartner, String>, JpaSpecificationExecutor<OfficialPartner> {

    List<OfficialPartner> findAllByType(String type);

}
