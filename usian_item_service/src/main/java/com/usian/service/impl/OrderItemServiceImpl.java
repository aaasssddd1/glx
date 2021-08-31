package com.usian.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.usian.pojo.OrderItem;
import com.usian.mapper.OrderItemMapper;
import com.usian.service.OrderItemService;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 
 * @since 2021-08-09
 */
@Service
public class OrderItemServiceImpl extends ServiceImpl<OrderItemMapper, OrderItem> implements OrderItemService {

}
