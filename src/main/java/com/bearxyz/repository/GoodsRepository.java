package com.bearxyz.repository;

import com.bearxyz.domain.po.business.Goods;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by bearxyz on 2017/5/25.
 */
public interface GoodsRepository extends JpaRepository<Goods, String> {

    List<Goods> findAllByNature(String nature);
}
