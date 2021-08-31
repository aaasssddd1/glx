package com.usian.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.usian.pojo.OrderItem;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author 
 * @since 2021-08-09
 */
public interface OrderItemMapper extends BaseMapper<OrderItem> {

    List<OrderItem> queryOverTimeOrderItems();

}
