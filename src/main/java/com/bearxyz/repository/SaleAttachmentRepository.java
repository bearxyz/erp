package com.bearxyz.repository;

import com.bearxyz.domain.po.business.SaleAttachment;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by bearxyz on 2017/9/2.
 */
public interface SaleAttachmentRepository extends JpaRepository<SaleAttachment, String> {
}
