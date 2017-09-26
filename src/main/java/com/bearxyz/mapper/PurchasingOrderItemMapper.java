package com.bearxyz.mapper;

import com.bearxyz.domain.po.business.PurchasingOrderItem;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface PurchasingOrderItemMapper {
    @Select("SELECT i.* FROM T_PURCHASING_ORDER_ITEM i " +
            "INNER JOIN T_GOODS g on g.ID=i.GOODS_ID " +
            "WHERE i.DATE_CEATED<=to_date(#{endDate}, 'yyyy-mm-dd') " +
            "AND i.DATE_CEATED>=to_date(#{startDate}, 'yyyy-mm-dd') " +
            "AND i.APPROVED=1 " +
            "AND g.PROJECT like '%'||#{project}||'%' " +
            "AND g.TYPE like '%'||#{type}||'%' " +
            "AND g.NAME like '%'||#{name}||'%'")
    List<PurchasingOrderItem> reportPurchasingOrderItem(@Param("startDate")String startDate, @Param("endDate")String endDate, @Param("project")String project, @Param("type")String type, @Param("name")String name);
}
