package com.bearxyz.repository;

import com.bearxyz.domain.po.business.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Created by bearxyz on 2017/7/27.
 */
public interface CompanyRepository extends JpaRepository<Company, String>, JpaSpecificationExecutor<Company> {

    @Query(value = "SELECT c.* FROM T_COMPANY c INNER JOIN T_USER_PROVINCE u ON c.PROVINCE=u.PROVINCE WHERE c.SIGNED = 1 AND u.USER_ID=?1", nativeQuery = true)
    List<Company> findCompaniesByUserProvince(String uid);

    @Query(value = "SELECT c.* FROM T_COMPANY c INNER JOIN T_USER_PROVINCE u ON c.PROVINCE=u.PROVINCE WHERE c.AUTO_ASSIGNED = 1 AND u.USER_ID=?1", nativeQuery = true)
    List<Company> findCompaniesByUserProvinceAutoSign(String uid);

    @Query(value = "SELECT c.* FROM T_COMPANY c INNER JOIN T_USER_CLIENT u ON c.ID=u.COMPANY_ID WHERE c.AUTO_ASSIGNED = 0 AND u.USER_ID=?1", nativeQuery = true)
    List<Company> findCompaniesByUserManSign(String uid);
}
