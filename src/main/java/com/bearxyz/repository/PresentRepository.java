package com.bearxyz.repository;

import com.bearxyz.domain.po.business.Present;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * Created by bearxyz on 2017/9/3.
 */
public interface PresentRepository extends JpaRepository<Present, String>, JpaSpecificationExecutor<Present> {
}
