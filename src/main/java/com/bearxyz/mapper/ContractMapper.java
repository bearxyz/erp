package com.bearxyz.mapper;

import com.bearxyz.domain.vo.ContractReport;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * Created by bearxyz on 2017/9/15.
 */
@Mapper
public interface ContractMapper {

    @Update("update T_CONTRACT set EXPIRED=1 WHERE END_DATE<trunc(sysdate, 'DD')")
    void checkContractValide();

    @Select("SELECT COUNT(PROJECT) as COUNT, PROJECT, SUM(PRESTORE) as PRESTORE, SUM(FEE) as FEE FROM T_CONTRACT " +
            "WHERE APPROVED=1 " +
            "AND DATE_CEATED<=to_date(#{endDate}, 'yyyy-mm-dd') " +
            "AND DATE_CEATED>=to_date(#{startDate}, 'yyyy-mm-dd') " +
            "AND PROJECT like '%'||#{project}||'%' " +
            "AND AGENT_PROVINCE like '%'||#{province}||'%' " +
            "GROUP BY PROJECT")
    List<ContractReport> getContractReportByProject(@Param("startDate")String startDate, @Param("endDate")String endDate, @Param("project")String project, @Param("province")String province);

    @Select("SELECT COUNT(CREATED_BY) as COUNT, CREATED_BY, SUM(PRESTORE) as PRESTORE, SUM(FEE) as FEE FROM T_CONTRACT " +
            "WHERE APPROVED=1 " +
            "AND DATE_CEATED<=to_date(#{endDate}, 'yyyy-mm-dd') " +
            "AND DATE_CEATED>=to_date(#{startDate}, 'yyyy-mm-dd') " +
            "AND PROJECT like '%'||#{project}||'%' " +
            "AND AGENT_PROVINCE like '%'||#{province}||'%' " +
            "GROUP BY CREATED_BY")
    List<ContractReport> getContractReportBySaleman(@Param("startDate")String startDate, @Param("endDate")String endDate, @Param("project")String project, @Param("province")String province);

}
