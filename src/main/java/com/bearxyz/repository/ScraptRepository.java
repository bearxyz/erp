package com.bearxyz.repository;

import com.bearxyz.domain.po.business.Scrapt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * Created by bearxyz on 2017/8/27.
 */
public interface ScraptRepository extends JpaRepository<Scrapt, String>, JpaSpecificationExecutor<Scrapt> {
}
