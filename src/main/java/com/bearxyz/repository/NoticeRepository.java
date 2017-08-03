package com.bearxyz.repository;

import com.bearxyz.domain.po.business.Notice;
import com.bearxyz.domain.vo.NoticeVO;
import org.springframework.data.jpa.domain.Specification;
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

}
