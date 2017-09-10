package com.bearxyz.repository;

import com.bearxyz.domain.po.business.Goods;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * Created by bearxyz on 2017/5/25.
 */
public interface GoodsRepository extends JpaRepository<Goods, String>, JpaSpecificationExecutor<Goods> {

    Integer countGoodsByStockLessThan(Integer alert);

}
