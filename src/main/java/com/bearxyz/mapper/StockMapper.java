package com.bearxyz.mapper;

import com.bearxyz.domain.po.business.StockItem;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface StockMapper {

    @Select("SELECT i.* FROM T_STOCK_ITEM i " +
            "INNER JOIN T_STOCK t ON i.STOCK_ID=t.ID " +
            "INNER JOIN T_GOODS g on g.ID=i.GOODS_ID " +
            "WHERE t.TYPE='STOCK-IN' " +
            "AND i.DATE_CEATED<=to_date(#{endDate}, 'yyyy-mm-dd') " +
            "AND i.DATE_CEATED>=to_date(#{startDate}, 'yyyy-mm-dd') " +
            "AND i.APPROVED=1 " +
            "AND g.PROJECT like '%'||#{project}||'%' " +
            "AND g.TYPE like '%'||#{type}||'%' " +
            "AND g.NAME like '%'||#{name}||'%'")
    List<StockItem> reportStockIn(@Param("startDate")String startDate, @Param("endDate")String endDate, @Param("project")String project, @Param("type")String type, @Param("name")String name);

}
