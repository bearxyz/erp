package com.bearxyz.repository;

import com.bearxyz.domain.po.business.PurchasingDetail;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by bearxyz on 2017/6/1.
 */
public interface PurchasingRequisitionDetailRepository extends JpaRepository<PurchasingDetail, Long> {

    Iterable<PurchasingDetail> findAllByPurchasingRequisitionId(long id);

}
