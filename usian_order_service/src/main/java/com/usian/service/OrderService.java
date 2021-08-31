package com.usian.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.usian.dto.OrderDTO;
import com.usian.pojo.Order;
import com.usian.vo.CartOrOrderItem;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 
 * @since 2021-08-09
 */
public interface OrderService extends IService<Order> {


    List<CartOrOrderItem> queryOrderItems(Long userId, Long[] ids);

    String insertOrder(OrderDTO orderDTO);
}
