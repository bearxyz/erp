package com.bearxyz.repository;

import com.bearxyz.domain.po.business.QA;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

/**
 * Created by bearxyz on 2017/9/10.
 */
public interface QARepository extends JpaRepository<QA, String>, JpaSpecificationExecutor<QA> {

    List<QA> findAllByType(String type);

}
