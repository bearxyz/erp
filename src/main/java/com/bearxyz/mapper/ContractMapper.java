package com.bearxyz.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;

/**
 * Created by bearxyz on 2017/9/15.
 */
@Mapper
public interface ContractMapper {

    @Update("update T_CONTRACT set EXPIRED=1 WHERE END_DATE<trunc(sysdate, 'DD')")
    void checkContractValide();

}
