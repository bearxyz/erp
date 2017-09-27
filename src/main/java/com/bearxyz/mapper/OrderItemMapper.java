package com.bearxyz.mapper;


import com.bearxyz.domain.vo.SaleReport;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface OrderItemMapper {

    @Select("select s.PROJECT,sum(p.AMMOUNT*oi.COUNT) as count, sum(oi.TOTAL_PRICE-oi.DISCOUNT_PRICE) as totalPrice from T_ORDER_ITEM oi " +
            "inner join T_ORDER o on o.ID=oi.ORDER_ID " +
            "INNER JOIN T_SALE s on s.ID=oi.SALE_ID " +
            "inner join T_PACKAGE p on p.ID=s.PACKAGE_ID " +
            "WHERE o.STATUS>=3 " +
            "AND oi.DATE_CEATED<=to_date(#{endDate}, 'yyyy-mm-dd') " +
            "AND oi.DATE_CEATED>=to_date(#{startDate}, 'yyyy-mm-dd') " +
            "group by s.project")
    List<SaleReport> getSaleReportByProject(@Param("startDate")String startDate, @Param("endDate")String endDate);

    @Select("select s.GOODS_ID as goods,s.PROJECT, s.TYPE,sum(p.AMMOUNT*oi.COUNT) as count, sum(oi.TOTAL_PRICE-oi.DISCOUNT_PRICE) as totalPrice from T_ORDER_ITEM oi " +
            "inner join T_ORDER o on o.ID=oi.ORDER_ID " +
            "INNER JOIN T_SALE s on s.ID=oi.SALE_ID " +
            "inner join T_PACKAGE p on p.ID=s.PACKAGE_ID " +
            "WHERE o.STATUS>=3 " +
            "AND s.PROJECT like '%'||#{project}||'%' " +
            "AND oi.DATE_CEATED<=to_date(#{endDate}, 'yyyy-mm-dd') " +
            "AND oi.DATE_CEATED>=to_date(#{startDate}, 'yyyy-mm-dd') " +
            "group by s.GOODS_ID,s.TYPE, s.PROJECT")
    List<SaleReport> getSaleReportByProduct(@Param("startDate")String startDate, @Param("endDate")String endDate, @Param("project")String project);

    @Select("select s.PROJECT,s.type, s.GOODS_ID as goods, c.AGENT_PROVINCE, c.AGENT_CITY, c.AGENT_DISTRICT,sum(p.AMMOUNT*oi.COUNT) as count, " +
            "sum(oi.TOTAL_PRICE-oi.DISCOUNT_PRICE) as totalPrice from T_ORDER_ITEM oi inner join T_ORDER o on o.ID=oi.ORDER_ID " +
            "inner join T_CONTRACT c on c.COMPANY_ID=o.COMPANY_ID INNER JOIN T_SALE s on s.ID=oi.SALE_ID " +
            "inner join T_PACKAGE p on p.ID=s.PACKAGE_ID " +
            "WHERE o.STATUS>=3 " +
            "AND s.PROJECT like '%'||#{project}||'%' " +
            "AND c.AGENT_PROVINCE like '%'||#{province}||'%' " +
            "AND c.AGENT_CITY like '%'||#{city}||'%' " +
            "AND c.AGENT_DISTRICT like '%'||#{district}||'%' " +
            "AND oi.DATE_CEATED<=to_date(#{endDate}, 'yyyy-mm-dd') " +
            "AND oi.DATE_CEATED>=to_date(#{startDate}, 'yyyy-mm-dd') " +
            "group by c.AGENT_PROVINCE,s.GOODS_ID,s.TYPE, c.AGENT_CITY, c.AGENT_DISTRICT, s.PROJECT")
    List<SaleReport> getSaleReportByProductAndProvince(@Param("startDate")String startDate, @Param("endDate")String endDate, @Param("project")String project, @Param("province")String province, @Param("city")String city, @Param("district")String district);

    @Select("select s.PROJECT,c.SIGN_PERSON, c.SIGN_PHONE, c.AGENT_PROVINCE, c.AGENT_CITY, c.AGENT_DISTRICT,sum(p.AMMOUNT*oi.COUNT) as count, " +
            "sum(oi.TOTAL_PRICE-oi.DISCOUNT_PRICE) as totalPrice from T_ORDER_ITEM oi inner join T_ORDER o on o.ID=oi.ORDER_ID " +
            "inner join T_CONTRACT c on c.COMPANY_ID=o.COMPANY_ID INNER JOIN T_SALE s on s.ID=oi.SALE_ID " +
            "inner join T_PACKAGE p on p.ID=s.PACKAGE_ID " +
            "WHERE o.STATUS>=3 " +
            "AND s.PROJECT like '%'||#{project}||'%' " +
            "AND c.SIGN_PERSON like '%'||#{signPerson}||'%' " +
            "AND c.AGENT_PROVINCE like '%'||#{province}||'%' " +
            "AND c.AGENT_CITY like '%'||#{city}||'%' " +
            "AND c.AGENT_DISTRICT like '%'||#{district}||'%' " +
            "AND oi.DATE_CEATED<=to_date(#{endDate}, 'yyyy-mm-dd') " +
            "AND oi.DATE_CEATED>=to_date(#{startDate}, 'yyyy-mm-dd') " +
            "group by c.AGENT_PROVINCE, c.SIGN_PERSON, c.SIGN_PHONE, c.AGENT_CITY, c.AGENT_DISTRICT, s.PROJECT")
    List<SaleReport> getSaleReportByClientAndProvince(@Param("startDate")String startDate, @Param("endDate")String endDate, @Param("project")String project, @Param("province")String province, @Param("city")String city, @Param("district")String district, @Param("signPerson")String signPerson);

    @Select("select up.USER_ID as customer,s.PROJECT,c.AGENT_PROVINCE, sum(p.AMMOUNT*oi.COUNT) as count, " +
            "            sum(oi.TOTAL_PRICE-oi.DISCOUNT_PRICE) as totalPrice from T_ORDER_ITEM oi inner join T_ORDER o on o.ID=oi.ORDER_ID " +
            "            inner join T_CONTRACT c on c.COMPANY_ID=o.COMPANY_ID INNER JOIN T_SALE s on s.ID=oi.SALE_ID " +
            "            inner join T_PACKAGE p on p.ID=s.PACKAGE_ID " +
            "            inner join T_USER_PROVINCE up on up.PROVINCE=c.AGENT_PROVINCE" +
            "            inner join SYS_ROLE_USER ur on ur.USER_ID = up.USER_ID" +
            "            inner join SYS_ROLE r on r.ID= ur.ROLE_ID" +
            "            WHERE o.STATUS>=3 AND r.MASK='ROLE_DEPARTMENT_SERVICE' " +
            "AND s.PROJECT like '%'||#{project}||'%' " +
            "AND c.AGENT_PROVINCE like '%'||#{province}||'%' " +
            "            group by c.AGENT_PROVINCE,s.PROJECT, up.USER_ID")
    List<SaleReport> getSaleReportByCustomerAndProvince(@Param("startDate")String startDate, @Param("endDate")String endDate, @Param("project")String project, @Param("province")String province);

}
