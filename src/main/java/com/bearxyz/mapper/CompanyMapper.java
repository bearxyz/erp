package com.bearxyz.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * Created by bearxyz on 2017/8/30.
 */
@Mapper
public interface CompanyMapper {

    @Insert("INSERT INTO T_USER_CLIENT(COMPANY_ID,USER_ID) VALUES(#{cid},#{uid})")
    void assignClientToUser(@Param("cid") String cid, @Param("uid") String uid);

}
