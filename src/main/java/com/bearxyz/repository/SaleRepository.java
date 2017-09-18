package com.bearxyz.repository;

import com.bearxyz.domain.po.business.Sale;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by bearxyz on 2017/8/30.
 */
public interface SaleRepository extends JpaRepository<Sale,String>, JpaSpecificationExecutor<Sale> {

    @Query(value = "SELECT s.* FROM T_SALE s INNER JOIN T_USER_SALE us ON s.ID=us.SALE_ID WHERE us.COMPANY_ID=?1 AND us.CATEGORY=?2", nativeQuery = true)
    List<Sale> findSalesByCompanyIdAndCategory(String companyId, String category);

    List<Sale> findAllByCategory(String category);

    @Query(value = "insert into T_USER_SALE(COMPANY_ID, SALE_ID, CATEGORY) values(?1,?2,?3)", nativeQuery = true)
    void insertCompanyUser(String companyId, String saleId, String category);

    @Query
    List<Sale> findAllByCategoryAndOnSale(String category, boolean onSale);

    @Query(value = "SELECT s.* FROM T_SALE s  WHERE s.CATEGORY in ?1", nativeQuery = true)
    List<Sale> getSaleByTypeList(List<String> category);

    @Modifying
    @Transactional
    @Query(value = "INSERT INTO T_COMPANY_SALE(COMPANY_ID,SALE_ID,SALE_PRICE,SALE_STATUS) VALUES (?1,?2,?3,?4)", nativeQuery = true)
    void insertCompanySale(String companyId, String saleId, Float salePrice, Integer saleStatus);

    @Query(value = "SELECT * FROM   T_COMPANY_SALE c   INNER  JOIN T_SALE s ON s.ID=c.SALE_ID WHERE  c.COMPANY_ID=?1 AND c.SALE_ID=?2", nativeQuery = true)
    List<Sale> findCompanySaleByCompanyIdAndSaleId(String companyId, String saleId);

    @Query(value = "SELECT s.* ,c.* FROM  T_SALE s INNER  JOIN T_COMPANY_SALE c ON s.ID=c.SALE_ID WHERE  c.COMPANY_ID=?1 ", nativeQuery = true)
    List<Sale> secSaleList(String companyId);

    @Query(value = "DELETE FROM T_COMPANY_SALE WHERE COMPANY_ID=?1 AND SALE_ID=?2", nativeQuery = true)
    void deleteCompaySale(String companyId, String SaleId);

    @Modifying
    @Transactional
    @Query(value = "UPDATE T_COMPANY_SALE SET SALE_PRICE=?1 WHERE COMPANY_ID=?2 AND SALE_ID=?3", nativeQuery = true)
    void updateCompaySale(Float salePrice, String companyId, String SaleId);


}
