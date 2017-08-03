package com.bearxyz.repository;

import com.bearxyz.domain.po.sys.Dict;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by bearxyz on 2017/6/3.
 */
public interface DictRepository extends JpaRepository<Dict, String> {

    List<Dict> findAllByParentId(String pid);

    List<Dict> findAllByParentMask(String pmask);

    Page<Dict> findDictsByParentId(String pid, Pageable request);

    int countAllByParentId(String pid);

    Dict findByMask(String mask);

}
