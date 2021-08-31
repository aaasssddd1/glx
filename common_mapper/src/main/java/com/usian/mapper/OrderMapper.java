package com.usian.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.usian.pojo.Order;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author 
 * @since 2021-08-09
 */
public interface OrderMapper extends BaseMapper<Order> {

    /**
     * 查询下单时间超过30分钟的 订单的订单号信息
     * @return
     */
//    public List<String> queryOverTimeOrderIds();

    /**
     * 关闭超时订单
     * @return
     */
    public int closeOverTimeOrders();

}
