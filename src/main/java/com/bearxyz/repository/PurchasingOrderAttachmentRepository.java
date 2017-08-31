package com.bearxyz.repository;

import com.bearxyz.domain.po.business.PurchasingOrderAttachment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * Created by bearxyz on 2017/8/31.
 */
public interface PurchasingOrderAttachmentRepository extends JpaRepository<PurchasingOrderAttachment, String>, JpaSpecificationExecutor<PurchasingOrderAttachment> {
}
