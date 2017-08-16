package com.bearxyz.repository;

import com.bearxyz.domain.po.business.ForUseItem;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by bearxyz on 2017/8/16.
 */
public interface ForUseItemRepository extends JpaRepository<ForUseItem, String> {
}
