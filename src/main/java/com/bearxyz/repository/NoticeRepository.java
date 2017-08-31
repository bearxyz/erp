package com.bearxyz.repository;

import com.bearxyz.domain.po.business.Notice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Created by bearxyz on 2017/6/1.
 */
public interface NoticeRepository extends JpaRepository<Notice, String>, JpaSpecificationExecutor<Notice> {

    void deleteById(String id);

    Notice findById(String id);

    @Query(value = "SELECT * FROM (SELECT * FROM T_NOTICE WHERE TYPE = ?2 ORDER BY LAST_UPDATED DESC) WHERE rownum<=?1", nativeQuery = true)
    List<Notice> findAllByTopAndType(int top, String type);

}
