package com.bearxyz.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * Created by bearxyz on 2017/9/4.
 */
@Mapper
public interface OrderMapper {

    @Insert("INSERT INTO T_USER_SALE(COMPANY_ID, SALE_ID, CATEGORY) VALUES(#{companyId}, #{saleId}, #{category})")
    void createSaleAndCompanyRelation(@Param("companyId")String companyId, @Param("saleId")String saleId, @Param("category")String category);

}
