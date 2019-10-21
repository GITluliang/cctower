package com.nuoze.cctower.dao;

import com.nuoze.cctower.pojo.entity.OrderNumber;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * @Author luliang
 * @Date 2019-10-21 16:00
 */
@Repository
public interface OrderNumberDAO extends BaseDAO<OrderNumber> {
    /**
     * 通过订单编号查询
     * @param orderSn 订单编号
     * @return OrderNumber
     */
    OrderNumber findByorderSn(@Param("orderSn") String orderSn);
}
