package com.bearxyz.repository;

import com.bearxyz.domain.po.business.ForUse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * Created by bearxyz on 2017/8/2.
 */
public interface ForUseRepository extends JpaRepository<ForUse, String>, JpaSpecificationExecutor<ForUse> {
}
