package com.bearxyz.repository;

import com.bearxyz.domain.po.business.Sale;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Created by bearxyz on 2017/8/30.
 */
public interface SaleRepository extends JpaRepository<Sale,String>, JpaSpecificationExecutor<Sale> {

    @Query(value = "SELECT s.* FROM T_SALE s INNER JOIN T_USER_SALE us ON s.ID=us.SALE_ID WHERE us.COMPANY_ID=?1 AND us.CATEGORY=?2", nativeQuery = true)
    List<Sale> findSalesByCompanyIdAndCategory(String companyId, String category);

}
