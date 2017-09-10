package com.bearxyz.repository;

import com.bearxyz.domain.po.business.Package;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Created by bearxyz on 2017/7/26.
 */
public interface PackageRepository extends JpaRepository<Package, String> {

    @Query(value = "SELECT  * from T_PACKAGE WHERE GOODS_ID=?1", nativeQuery = true)
    List<Package> findAllByGoodsId(String id);

}
