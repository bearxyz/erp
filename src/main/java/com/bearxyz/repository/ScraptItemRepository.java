package com.bearxyz.repository;

import com.bearxyz.domain.po.business.ScraptItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * Created by bearxyz on 2017/8/27.
 */
public interface ScraptItemRepository extends JpaRepository<ScraptItem, String>, JpaSpecificationExecutor<ScraptItem> {
}
